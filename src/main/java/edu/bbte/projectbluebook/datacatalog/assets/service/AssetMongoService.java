package edu.bbte.projectbluebook.datacatalog.assets.service;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import edu.bbte.projectbluebook.datacatalog.assets.model.AssetRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.Parameter;
import edu.bbte.projectbluebook.datacatalog.assets.repository.AssetMongoRepository;
import edu.bbte.projectbluebook.datacatalog.assets.util.AzureBlobUtil;
import edu.bbte.projectbluebook.datacatalog.assets.util.AzureBlobUtilException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class AssetMongoService {

    @Autowired
    private AssetMongoRepository repository;

    private boolean isLowerCaseLetter(char letter) {
        return 'a' <= letter && letter <= 'z';
    }

    private boolean isUpperCaseLetter(char letter) {
        return 'A' <= letter && letter <= 'Z';
    }

    private String caseInsensitiveRegexCreator(String keyword) {
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

    private AssetResponse getResponseFromAssetDoc(Document doc) {
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

    public ResponseEntity<Void> createAsset(@Valid AssetRequest assetRequest) {
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

        if (assetRequest.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Assets must have a name!");
        }
        if (assetRequest.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Assets must have a description!");
        }

        Document parameters = new Document();
        Document location = new Document();

        assetLocation
                .getParameters()
                .stream()
                .forEach(item -> parameters.append(item.getKey(), item.getValue()));
        location.append("type", assetRequest.getLocation().getType());
        location.append("parameters", parameters);

        Document asset = new Document();
        asset.append("name", assetRequest.getName());
        asset.append("description", assetRequest.getDescription());
        asset.append("location", location);
        asset.append("tags", assetRequest.getTags());
        asset.append("format", assetRequest.getFormat().getValue());
        try {
            asset.append("size", Double.valueOf(assetRequest.getSize()));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Size must be a positive number (size in MB).");
        }
        asset.append("namespace", assetRequest.getNamespace());
        asset.append("visited", Long.valueOf(0));

        return repository.insert(asset)
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

    }

    public ResponseEntity<Void> deleteAsset(String assetId) {
        Document deleted;
        try {
            Document id = new Document("_id", new ObjectId(assetId));
            deleted = repository.delete(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (deleted == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<AssetResponse> getAsset(String assetId) {
        Document doc;
        try {
            Document id = new Document("_id", new ObjectId(assetId));
            Document update = new Document("$inc", new Document("visited", 1));
            doc = repository.findAndUpdate(id, update);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (doc == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (Long.MAX_VALUE - doc.getLong("visited") < 5000) {
            Document update = new Document("$inc", new Document("visited", - 10000));
            Document id = new Document("_id", new ObjectId(assetId));
            if (!repository.update(id, update)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        AssetResponse assetResponse = getResponseFromAssetDoc(doc);
        return new ResponseEntity<>(assetResponse, HttpStatus.OK);
    }

    public ResponseEntity<List<AssetResponse>> getAssets(@Valid List<String> tags, @Valid String namespace) {
        Document filter = new Document();
        if (namespace != null && !namespace.isBlank()) {
            filter.append("namespace", namespace);
        }
        if (tags != null && !tags.isEmpty()) {
            filter.append("tags", new Document("$elemMatch", new Document("$in", tags)));
        }
        FindIterable<Document> docs = repository.findByVisited(filter);

        List<AssetResponse> filtered = new ArrayList<>();
        for (Document doc : docs) {
            filtered.add(getResponseFromAssetDoc(doc));
        }
        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }

    public ResponseEntity<Void> patchAsset(String assetId, @Valid AssetRequest assetRequest) {
        Document filter;
        try {
            filter = new Document("_id", new ObjectId(assetId));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Document set = new Document();
        if (assetRequest.getName() != null) {
            if (assetRequest.getName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Assets must have a name!");
            }
            set.append("name", assetRequest.getName());
        }
        if (assetRequest.getDescription() != null) {
            if (assetRequest.getDescription().isBlank()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Assets must have a description!");
            }
            set.append("description", assetRequest.getDescription());
        }
        if (assetRequest.getFormat() != null) {
            set.append("format", assetRequest.getFormat());
        }
        if (assetRequest.getNamespace() != null) {
            set.append("namespace", assetRequest.getNamespace());
        }
        if (assetRequest.getSize() != null) {
            try {
                double size = Double.valueOf(assetRequest.getSize());
                if (size < 0) {
                    throw new ResponseStatusException(
                            HttpStatus.UNPROCESSABLE_ENTITY,
                            "Size must be a positive number (size in MB).");
                }
                set.append("size", size);
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Size must be a positive number (size in MB).");
            }
        }
        if (assetRequest.getTags() != null && assetRequest.getTags().size() != 0) {
            set.append("tags", assetRequest.getTags());
        }
        if (assetRequest.getLocation() != null) {
            if (assetRequest.getLocation().getParameters().size() == 0) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Location object must have parameters!");
            }
            if (assetRequest.getLocation().getType().isBlank()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Location object must have a type!");
            }
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
        Document update = new Document();
        update.append("$set", set);
        update.append("$currentDate", new Document("updatedAt", true));
        Document old = repository.findAndUpdate(filter, update);
        if (old == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Void> addTag(@Size(min = 1) String tag, String assetId) {
        Document id;
        try {
            id = new Document("_id", new ObjectId(assetId));
        } catch (MongoException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Document tags = new Document("tags", tag);
        Document update = new Document("$addToSet", tags);
        Document asset = repository.findAndUpdate(id, update);
        if (asset == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Void> deleteTag(@Size(min = 1) String tag, String assetId) {
        Document id;
        try {
            id = new Document("_id", new ObjectId(assetId));
        } catch (MongoException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Document tags = new Document("tags", tag);
        Document update = new Document("$pull", tags);
        Document asset = repository.findAndUpdate(id, update);
        if (asset == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<List<AssetResponse>> searchAssets(
            String keyword,
            @Valid List<String> tags,
            @Valid String namespace) {
        String regex = caseInsensitiveRegexCreator(keyword);
        Document filter = new Document();
        if (!keyword.isBlank()) {
            filter.append("name", new Document("$regex", regex));
        }
        if (namespace != null && !namespace.isBlank()) {
            filter.append("namespace", namespace);
        }
        if (tags != null && !tags.isEmpty()) {
            filter.append("tags", new Document("$elemMatch", new Document("$in", tags)));
        }
        FindIterable<Document> docs = repository.findByVisited(filter);

        List<AssetResponse> filtered = new ArrayList<>();
        for (Document doc : docs) {
            filtered.add(getResponseFromAssetDoc(doc));
        }
        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }
}
