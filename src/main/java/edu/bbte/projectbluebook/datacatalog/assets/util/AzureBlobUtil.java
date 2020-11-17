package edu.bbte.projectbluebook.datacatalog.assets.util;

import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.Parameter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

public class AzureBlobUtil {
    public static Location extractLocationParameters(Location location) {
        // extract SAS token from location parameters
        String token = location.getParameters()
                .stream()
                .filter(parameter -> parameter.getKey().equals("sasToken"))
                .findFirst()
                .map(Parameter::getValue)
                .get();

        // parse SAS token into key value pairs
        final MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUriString(token).build().getQueryParams();

        location.addParametersItem(buildParameter("creationTime", queryParams.getFirst("st")));
        location.addParametersItem(buildParameter("expiryTime", queryParams.getFirst("se")));
        location.addParametersItem(buildParameter("permissions", queryParams.getFirst("sp")));

        return location;
    }

    private static Parameter buildParameter(String key, String value) {
        Parameter parameter = new Parameter();
        parameter.setKey(key);
        parameter.setValue(value);

        return parameter;
    }
}
