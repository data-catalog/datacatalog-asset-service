package edu.bbte.projectbluebook.datacatalog.assets.util;

import edu.bbte.projectbluebook.datacatalog.assets.exception.ValidationException;
import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.Parameter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class LocationValidator {
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
        Optional<String> sasToken = getLocationParameter(location, "sasToken");

        if (sasToken.isPresent()) {
            return extractSasTokenParameters(location, sasToken.get());
        }

        if (getLocationParameter(location, "accountKey").isPresent()) {
            return location;
        }

        throw new ValidationException("No access token found.");
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
