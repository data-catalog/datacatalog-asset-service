package edu.bbte.projectbluebook.datacatalog.assets.helpers;

import edu.bbte.projectbluebook.datacatalog.assets.model.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.Parameter;
import org.bson.Document;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Utility {

    public static boolean isLowerCaseLetter(char letter) {
        return 'a' <= letter && letter <= 'z';
    }

    public static boolean isUpperCaseLetter(char letter) {
        return 'A' <= letter && letter <= 'Z';
    }

    public static String caseInsensitiveRegexCreator(String keyword) {
        StringBuffer regex = new StringBuffer("");
        for (int i = 0; i < keyword.length(); i++) {
            char current = keyword.charAt(i);
            if (isLowerCaseLetter(current)) {
                regex.append("[" + current + String.valueOf(current).toUpperCase(Locale.US) + "]");
            } else {
                if (isUpperCaseLetter(current)) {
                    regex.append("[" + String.valueOf(current).toLowerCase(Locale.US) + current + "]");
                } else {
                    regex.append(current);
                }
            }
            regex.append("[ \\n]*");
        }
        return regex.toString();
    }

    public static AssetResponse getResponseFromAssetDoc(Document doc) {
        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setId(doc.getObjectId("_id").toString());
        assetResponse.setName(doc.getString("name"));
        assetResponse.setDescription(doc.getString("description"));
        assetResponse.setNamespace(doc.getString("namespace"));
        assetResponse.setFormat(doc.getString("format").equals("json")
                ? AssetResponse.FormatEnum.JSON
                : AssetResponse.FormatEnum.CSV);
        assetResponse.setCreatedAt(doc.getDate("createdAt").toInstant().atOffset(ZoneOffset.UTC));
        assetResponse.setUpdatedAt(doc.getDate("updatedAt").toInstant().atOffset(ZoneOffset.UTC));
        assetResponse.setTags(doc.getList("tags", String.class));
        Location assetLocation = new Location();
        Document location = (Document)doc.get("location");
        assetLocation.setType(location.getString("type"));
        List<Parameter> parameters = new ArrayList<>();
        Document locationParameters = (Document)location.get("parameters");
        Set loc = locationParameters.keySet();
        loc.stream().forEach(item -> {
            Parameter parameter = new Parameter();
            parameter.setKey(item.toString());
            parameter.setValue(locationParameters.getString(item));
            parameters.add(parameter);
        });
        assetLocation.setParameters(parameters);
        assetResponse.setLocation(assetLocation);
        return assetResponse;
    }
}
