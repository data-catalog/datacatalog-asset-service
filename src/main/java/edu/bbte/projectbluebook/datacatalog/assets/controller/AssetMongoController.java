package edu.bbte.projectbluebook.datacatalog.assets.controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import edu.bbte.projectbluebook.datacatalog.assets.api.AssetApi;
import edu.bbte.projectbluebook.datacatalog.assets.model.*;
import edu.bbte.projectbluebook.datacatalog.assets.util.AzureBlobUtil;
import edu.bbte.projectbluebook.datacatalog.assets.util.AzureBlobUtilException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
public class AssetMongoController implements AssetApi  {

    private static MongoClientURI uri = new MongoClientURI("mongodb+srv://m001-student:m001-mongodb-basics@cluster0.dlhll.mongodb.net/DataCatalog?retryWrites=true&w=majority");
    private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("DataCatalog");
    private static MongoCollection<Document> assets = database.getCollection("Assets");

    private boolean isLowerCaseLetter(char letter) {
        return 'a' <= letter && letter <= 'z';
    }

    private boolean isUpperCaseLetter(char letter) {
        return 'A' <= letter && letter <= 'Z';
    }

    private String caseInsensitiveRegexCreator(String keyword) {
        String regex = "";
        for (int i = 0; i < keyword.length(); i++) {
            char current = keyword.charAt(i);
            if (isLowerCaseLetter(current)) {
                regex += "[" + current + (current + "").toUpperCase() + "]";
            } else {
                if (isUpperCaseLetter(current)) {
                    regex += "[" + (current + "").toLowerCase() + current + "]";
                } else {
                    regex += current;
                }
            }
            regex += "[ \\n]*";
        }
        return regex;
    }


    @Override
    public ResponseEntity<Void> createAsset(@Valid AssetRequest assetRequest) {
        Document asset = new Document();

        // current date time
        //LocalDateTime currentTime = LocalDateTime.now();
        Date currentTime = new Date();

        Document location = new Document();
        Document parameters = new Document();

        Location assetLocation;
        if (assetRequest.getLocation().getType().equals("azureblob")) {
            try {
                assetLocation = AzureBlobUtil.extractLocationParameters(assetRequest.getLocation());
            } catch (AzureBlobUtilException e) {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            assetLocation = assetRequest.getLocation();
        }

       assetLocation
                .getParameters()
                .stream()
                .forEach(item -> parameters.append(item.getKey(), item.getValue()));
        location.append("type", assetRequest.getLocation().getType());
        location.append("parameters", parameters);

        asset.append("createdAt", currentTime);
        asset.append("updatedAt", currentTime);
        asset.append("name", assetRequest.getName());
        asset.append("description", assetRequest.getDescription());
        asset.append("location", location);
        asset.append("tags", assetRequest.getTags());
        asset.append("format", assetRequest.getFormat().getValue());
        asset.append("size", Double.valueOf(assetRequest.getSize()));
        asset.append("namespace", assetRequest.getNamespace());
        asset.append("visited", Long.valueOf(0));

        try {
            assets.insertOne(asset);
        } catch (Exception e) {
            return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteAsset(String assetId) {
        Document deleted;
        try {
            deleted = assets.findOneAndDelete(new Document("_id", new ObjectId(assetId)));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        if (deleted == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<AssetResponse> getAsset(String assetId) {
        Document update = new Document("$inc", new Document("visited", 1));
        Document doc;
        try {
            doc = assets.findOneAndUpdate(new Document("_id", new ObjectId(assetId)), update);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<AssetResponse>(HttpStatus.NOT_FOUND);
        }
        if (doc == null) {
            return new ResponseEntity<AssetResponse>(HttpStatus.NOT_FOUND);
        }
        if (Long.MAX_VALUE - doc.getLong("visited") < 5000) {
            update = new Document("$inc", new Document("visited", - 10000));
            assets.updateOne(new Document("_id", new ObjectId(assetId)), update);
        }
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
        assetResponse.setSize(doc.get("size").toString());
        assetResponse.setTags(doc.getList("tags", String.class));
        Location assetLocation = new Location();
        Document location = (Document)doc.get("location");
        assetLocation.setType(location.getString("type"));
        List<Parameter> parameters = new ArrayList<>();
        Document locationParameters = ((Document)location.get("parameters"));
        Set loc = locationParameters.keySet();
        loc.stream().forEach(item -> {
            Parameter parameter = new Parameter();
            parameter.setKey(item.toString());
            parameter.setValue(locationParameters.getString(item));
            parameters.add(parameter);
        });
        assetLocation.setParameters(parameters);
        assetResponse.setLocation(assetLocation);
        return new ResponseEntity<AssetResponse>(assetResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AssetResponse>> getAssets(@Valid List<String> tags, @Valid String namespace) {
        Document filter = new Document();
        if (namespace != null && !namespace.isBlank()) {
            filter.append("namespace", namespace);
        }
        if (tags != null && !tags.isEmpty()) {
            tags.stream().forEach(item -> System.out.println(item));
            filter.append("tags", new Document("$elemMatch", new Document("$in", tags)));
        }
        FindIterable<Document> docs = assets.find(filter).sort(new Document("visited", - 1));

        List<AssetResponse> filtered = new ArrayList<>();
        for (Document doc : docs) {
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
            assetResponse.setSize(doc.get("size").toString());
            assetResponse.setTags(doc.getList("tags", String.class));
            Location assetLocation = new Location();
            Document location = (Document)doc.get("location");
            assetLocation.setType(location.getString("type"));
            List<Parameter> parameters = new ArrayList<>();
            Document locationParameters = ((Document)location.get("parameters"));
            Set loc = locationParameters.keySet();
            loc.stream().forEach(item -> {
                Parameter parameter = new Parameter();
                parameter.setKey(item.toString());
                parameter.setValue(locationParameters.getString(item));
                parameters.add(parameter);
            });
            assetLocation.setParameters(parameters);
            assetResponse.setLocation(assetLocation);
            filtered.add(assetResponse);
        }
        return new ResponseEntity<List<AssetResponse>>(filtered, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> patchAsset(String assetId, @Valid AssetRequest assetRequest) {
        Document filter;
        try {
            filter = new Document("_id", new ObjectId(assetId));
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Document update = new Document();
        Document set = new Document();
        if (assetRequest.getName() != null) {
            set.append("name", assetRequest.getName());
        }
        if (assetRequest.getDescription() != null) {
            set.append("description", assetRequest.getDescription());
        }
        if (assetRequest.getFormat() != null) {
            set.append("format", assetRequest.getFormat());
        }
        if (assetRequest.getNamespace() != null) {
            set.append("namespace", assetRequest.getNamespace());
        }
        if (assetRequest.getSize() != null) {
            set.append("size", Double.valueOf(assetRequest.getSize()));
        }
        if (assetRequest.getTags() != null && assetRequest.getTags().size() != 0) {
            set.append("tags", assetRequest.getTags());
        }
        if (assetRequest.getLocation() != null
                && (assetRequest.getLocation().getParameters().size() != 0
                || !assetRequest.getLocation().getType().isBlank())) {
            Document location = new Document();
            Document parameters = new Document();
            assetRequest
                    .getLocation()
                    .getParameters()
                    .stream()
                    .forEach(item -> {
                        parameters.append(item.getKey(), item.getValue());
                    });
            location.append("type", assetRequest.getLocation().getType());
            location.append("parameters", parameters);
            set.append("location", location);
        }
        update.append("$set", set);
        update.append("$currentDate", new Document("updatedAt", true));
        Document old = assets.findOneAndUpdate(filter, update);
        if (old == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> addTag(String assetId, @Valid String body) {
        Document update = new Document("tags", body);
        Document id;
        try {
            id = new Document("_id", new ObjectId(assetId));
        } catch(MongoException e) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        Document asset = assets.findOneAndUpdate(id, new Document("$addToSet", update));
        if (asset == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteTag(String asssetId, @Size(min = 1) String tag) {
        Document update = new Document("tags", tag);
        Document id;
        try {
            id = new Document("_id", new ObjectId(asssetId));
        } catch(MongoException e) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        Document asset = assets.findOneAndUpdate(id, new Document("$pull", update));
        if (asset == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<AssetResponse>> searchAssets(String keyword, @Valid List<String> tags, @Valid String namespace) {
        String regex = caseInsensitiveRegexCreator(keyword);
        Document filter = new Document()
                .append("name", new Document("$regex", regex));
        FindIterable<Document> docs = assets.find(filter);

        List<AssetResponse> filtered = new ArrayList<>();
        for (Document doc : docs) {
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
            assetResponse.setSize(doc.get("size").toString());
            assetResponse.setTags(doc.getList("tags", String.class));
            Location assetLocation = new Location();
            Document location = (Document)doc.get("location");
            assetLocation.setType(location.getString("type"));
            List<Parameter> parameters = new ArrayList<>();
            Document locationParameters = ((Document)location.get("parameters"));
            Set loc = locationParameters.keySet();
            loc.stream().forEach(item -> {
                Parameter parameter = new Parameter();
                parameter.setKey(item.toString());
                parameter.setValue(locationParameters.getString(item));
                parameters.add(parameter);
            });
            assetLocation.setParameters(parameters);
            assetResponse.setLocation(assetLocation);
            filtered.add(assetResponse);
        }
        return new ResponseEntity<List<AssetResponse>>(filtered, HttpStatus.OK);
    }
}
