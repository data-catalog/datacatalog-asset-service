package edu.bbte.projectbluebook.datacatalog.assets.util;

import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.Parameter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class LocationValidator {
    public static Location validateLocation(Location location) throws LocationValidatorException {
        return (location.getType().equals("azureblob")) ? validateAzureBlobLocation(location) : validateUrlLocation(location);
    }

    private static Location validateUrlLocation(Location location) throws LocationValidatorException {
        if (getLocationParameter(location, "url").isEmpty()) {
            throw new LocationValidatorException("No url location parameter found.");
        }

        return location;
    }

    private static Location validateAzureBlobLocation(Location location) throws LocationValidatorException {
        Optional<String> sasToken = getLocationParameter(location, "sasToken");

        if (sasToken.isPresent()) {
            return extractSasTokenParameters(location, sasToken.get());
        }

        if (getLocationParameter(location, "accountKey").isPresent()) {
            return location;
        }

        throw new LocationValidatorException("No access token found.");
    }

    private static Location extractSasTokenParameters(Location location, String token) throws LocationValidatorException {
        // parse SAS token into key value pairs
        final MultiValueMap<String, String> queryParams = UriComponentsBuilder
                .fromUriString(token)
                .build()
                .getQueryParams();

        if (!queryParams.containsKey("st")
                || !queryParams.containsKey("se")
                || !queryParams.containsKey("sp")) {
            throw new LocationValidatorException("Invalid SAS token format.");
        }
        
        location.addParametersItem(buildParameter("creationTime", queryParams.getFirst("st")));
        location.addParametersItem(buildParameter("expiryTime", queryParams.getFirst("se")));
        location.addParametersItem(buildPermissionParameter(Objects.requireNonNull(queryParams.getFirst("sp"))));

        return location;
    }

    private static Parameter buildParameter(String key, String value) {
        Parameter parameter = new Parameter();
        parameter.setKey(key);
        parameter.setValue(value);

        return parameter;
    }

    private static Parameter buildPermissionParameter(String permissionString) throws LocationValidatorException {
        if (!permissionString.matches("^[lrwcd]*$")) {
            throw new LocationValidatorException("Invalid permission string");
        }

        ArrayList<String> permissions = new ArrayList<>();
        
        if (permissionString.contains("l")) {
            permissions.add("list");
        }
        if (permissionString.contains(("r"))) {
            permissions.add("read");
        }
        if (permissionString.contains(("w"))) {
            permissions.add("write");
        }
        if (permissionString.contains(("c"))) {
            permissions.add("create");
        }
        if (permissionString.contains(("d"))) {
            permissions.add("delete");
        }

        StringJoiner joiner = new StringJoiner("#");
        permissions.forEach(joiner::add);

        return buildParameter("permissions", joiner.toString());
    }

    private static Optional<String> getLocationParameter(Location location, String key) {
        return location.getParameters()
                .stream()
                .filter(parameter -> parameter.getKey().equals(key))
                .findFirst()
                .map(Parameter::getValue);
    }
}
