package edu.bbte.projectbluebook.datacatalog.assets.util;

import edu.bbte.projectbluebook.datacatalog.assets.config.EncryptionProperties;
import edu.bbte.projectbluebook.datacatalog.assets.exception.ValidationException;
import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.Parameter;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class LocationProcessor {
    private final TextEncryptor encryptor;

    public LocationProcessor(EncryptionProperties encryptionProperties) {
        encryptor = Encryptors.text(encryptionProperties.getPassword(), encryptionProperties.getSalt());
    }

    public Location validateLocation(Location location) {
        if (location.getType().equals("azureblob")) {
            return validateAzureBlobLocation(location);
        }

        return validateUrlLocation(location);
    }

    private Location validateUrlLocation(Location location) {
        if (getLocationParameter(location, "url").isEmpty()) {
            throw new ValidationException("No url location parameter found.");
        }

        return location;
    }

    private Location validateAzureBlobLocation(Location location) {
        if (getLocationParameter(location, "accountUrl").isEmpty()) {
            throw new ValidationException("Account URL not found.");
        }

        if (getLocationParameter(location, "containerName").isEmpty()) {
            throw new ValidationException("Container name not found.");
        }

        Optional<String> sasToken = getLocationParameter(location, "sasToken");
        Optional<String> accountKey = getLocationParameter(location, "accountKey");

        if (sasToken.isEmpty() && accountKey.isEmpty()) {
            throw new ValidationException("No SAS token or accont key found.");
        }

        sasToken.ifPresent(token -> extractSasTokenParameters(location, token));

        return location;
    }

    public Location encryptTokens(Location location) {
        List<Parameter> encryptedParameters = location.getParameters().stream().peek(parameter -> {
            if (parameter.getKey().equals("sasToken") || parameter.getKey().equals("accountKey")) {
                parameter.setValue(encryptor.encrypt(parameter.getValue()));
            }
        }).collect(Collectors.toList());

        return location.setParameters(encryptedParameters);
    }

    public Location decryptTokens(Location location) {
        List<Parameter> decryptedParameters = location.getParameters().stream().peek(parameter -> {
            if (parameter.getKey().equals("sasToken") || parameter.getKey().equals("accountKey")) {
                parameter.setValue(encryptor.decrypt(parameter.getValue()));
            }
        }).collect(Collectors.toList());

        return location.setParameters(decryptedParameters);
    }

    private Location extractSasTokenParameters(Location location, String token) {
        // parse SAS token into key value pairs
        final MultiValueMap<String, String> queryParams = UriComponentsBuilder
                .fromUriString(token)
                .build()
                .getQueryParams();

        if (!queryParams.containsKey("st")
                || !queryParams.containsKey("se")
                || !queryParams.containsKey("sp")) {
            throw new ValidationException("Invalid SAS token format.");
        }

        location.getParameters().add(buildParameter("creationTime", queryParams.getFirst("st")));
        location.getParameters().add(buildParameter("expiryTime", queryParams.getFirst("se")));
        location.getParameters().add(buildPermissionParameter(Objects.requireNonNull(queryParams.getFirst("sp"))));

        return location;
    }

    private Parameter buildParameter(String key, String value) {
        Parameter parameter = new Parameter();
        parameter.setKey(key);
        parameter.setValue(value);

        return parameter;
    }

    private Parameter buildPermissionParameter(String permissionString) {
        if (!permissionString.matches("^[lrwcd]*$")) {
            throw new ValidationException("Invalid permission string.");
        }

        ArrayList<String> permissions = new ArrayList<>();

        if (permissionString.contains("l")) {
            permissions.add("list");
        }
        if (permissionString.contains("r")) {
            permissions.add("read");
        }
        if (permissionString.contains("w")) {
            permissions.add("write");
        }
        if (permissionString.contains("c")) {
            permissions.add("create");
        }
        if (permissionString.contains("d")) {
            permissions.add("delete");
        }

        StringJoiner joiner = new StringJoiner("#");
        permissions.forEach(joiner::add);

        return buildParameter("permissions", joiner.toString());
    }

    private Optional<String> getLocationParameter(Location location, String key) {
        return location.getParameters()
                .stream()
                .filter(parameter -> parameter.getKey().equals(key))
                .findFirst()
                .map(Parameter::getValue);
    }
}
