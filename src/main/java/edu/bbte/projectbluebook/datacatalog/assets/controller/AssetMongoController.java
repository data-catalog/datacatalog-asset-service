package edu.bbte.projectbluebook.datacatalog.assets.controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.bbte.projectbluebook.datacatalog.assets.api.AssetApi;
import edu.bbte.projectbluebook.datacatalog.assets.model.AssetRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.Parameter;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @Override
    public ResponseEntity<Void> createAsset(@Valid AssetRequest assetRequest) {
        Document asset = new Document();

        // current date time
        //LocalDateTime currentTime = LocalDateTime.now();
        Date currentTime = new Date();

        Document location = new Document();
        Document parameters = new Document();
        assetRequest
                .getLocation()
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

        try {
            assets.insertOne(asset);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteAsset(String assetId) {
        Document deleted = assets.findOneAndDelete(new Document("_id", new ObjectId(assetId)));
        if (deleted == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<AssetResponse> getAsset(String assetId) {
        FindIterable<Document> docs = assets
                .find(new Document("_id", new ObjectId(assetId)))
                .limit(1);
        boolean found = false;
        AssetResponse assetResponse = new AssetResponse();
        for (Document doc : docs) {
            if (doc == null) {
                return new ResponseEntity<AssetResponse>(HttpStatus.NOT_FOUND);
            }
            found = true;
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
        }
        if (!found) {
            return new ResponseEntity<AssetResponse>(HttpStatus.NOT_FOUND);
        }
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

    @Override
    public ResponseEntity<Void> patchAsset(String assetId, @Valid AssetRequest assetRequest) {
        Document filter = new Document("_id", new ObjectId(assetId));
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
            return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}