package edu.bbte.projectbluebook.datacatalog.assets.service;

import com.mongodb.client.FindIterable;
import edu.bbte.projectbluebook.datacatalog.assets.helpers.Utility;
import edu.bbte.projectbluebook.datacatalog.assets.model.*;
import edu.bbte.projectbluebook.datacatalog.assets.repository.AssetMongoRepository;
import edu.bbte.projectbluebook.datacatalog.assets.util.LocationValidator;
import edu.bbte.projectbluebook.datacatalog.assets.util.LocationValidatorException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.*;

@Service
public class AssetMongoService {

    @Autowired
    private AssetMongoRepository repository;

    public ResponseEntity<Void> createAsset(@Valid AssetCreationRequest assetRequest, String uid) {
        Location assetLocation;
        try {
            assetLocation = LocationValidator.validateLocation(assetRequest.getLocation());
        } catch (LocationValidatorException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
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
        asset.append("shortDescription", assetRequest.getShortDescription());
        asset.append("location", location);
        asset.append("tags", assetRequest.getTags());
        asset.append("format", assetRequest.getFormat().getValue());
        asset.append("namespace", assetRequest.getNamespace());
        asset.append("visited", Long.valueOf(0));
        asset.append("owner", uid);
        asset.append("markedAsFavorite", new ArrayList<>());

        return repository.insert(asset)
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

    }

    public ResponseEntity<Void> deleteAsset(String assetId, String uid, String role) {
        Document deleted;
        try {
            Document id = new Document("_id", new ObjectId(assetId));
            if (role.equals("admin") || repository
                    .findAndUpdate(id, new Document())
                    .get("owner").equals(uid)) {
                deleted = repository.delete(id);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (deleted == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<AssetResponse> getAsset(String assetId, String uid, String role) {
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
        AssetResponse assetResponse;
        if (role.equals("admin") || doc.get("owner").equals(uid)) {
            assetResponse = Utility.getResponseFromAssetDoc(doc);
        } else {
            assetResponse = Utility.getResponseViewFromAssetDoc(doc);
        }
        return new ResponseEntity<>(assetResponse, HttpStatus.OK);
    }

    public ResponseEntity<List<AssetResponse>> getAssets(
            @Valid List<String> tags,
            @Valid String namespace,
            String uid,
            String role) {
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
            if (role.equals("admin") || doc.get("owner").equals(uid)) {
                filtered.add(Utility.getResponseFromAssetDoc(doc));
            } else {
                filtered.add(Utility.getResponseViewFromAssetDoc(doc));
            }
        }
        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }

    public ResponseEntity<Void> patchAsset(String assetId, @Valid AssetUpdateRequest assetRequest, String uid, String role) {
        try {
            Document id = new Document("_id", new ObjectId(assetId));
            if (!role.equals("admin") && !repository
                    .findAndUpdate(id, new Document())
                    .get("owner").equals(uid)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Document filter;
        try {
            filter = new Document("_id", new ObjectId(assetId));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
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
        if (assetRequest.getShortDescription() != null) {
            if (assetRequest.getDescription().isBlank()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Assets must have a short description!");
            }
            set.append("shortDescription", assetRequest.getShortDescription());
        }
        if (assetRequest.getFormat() != null) {
            set.append("format", assetRequest.getFormat());
        }
        if (assetRequest.getNamespace() != null) {
            set.append("namespace", assetRequest.getNamespace());
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
        Document old = repository.findAndUpdateMark(filter, update);
        if (old == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Void> addTag(@Size(min = 1) String tag, String assetId, String uid, String role) {
        try {
            Document id = new Document("_id", new ObjectId(assetId));
            if (!role.equals("admin") && !repository
                    .findAndUpdate(id, new Document())
                    .get("owner").equals(uid)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Document id;
        try {
            id = new Document("_id", new ObjectId(assetId));
        } catch (IllegalArgumentException e) {
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

    public ResponseEntity<Void> deleteTag(@Size(min = 1) String tag, String assetId, String uid, String role) {
        try {
            Document id = new Document("_id", new ObjectId(assetId));
            if (!role.equals("admin") && !repository
                    .findAndUpdate(id, new Document())
                    .get("owner").equals(uid)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Document id;
        try {
            id = new Document("_id", new ObjectId(assetId));
        } catch (IllegalArgumentException e) {
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
            @Valid String namespace,
            @Valid String owner,
            String uid,
            String role) {
        Document filter = new Document();
        if (keyword != null && !keyword.isBlank() && !keyword.equals("#")) {
            String regex = Utility.caseInsensitiveRegexCreator(keyword);
            filter.append("name", new Document("$regex", regex));
        }
        if (namespace != null && !namespace.isBlank()) {
            filter.append("namespace", namespace);
        }
        if (tags != null && !tags.isEmpty()) {
            filter.append("tags", new Document("$elemMatch", new Document("$in", tags)));
        }
        if (owner != null && !owner.isBlank()) {
            filter.append("owner", owner);
        }
        FindIterable<Document> docs = repository.findByVisited(filter);

        List<AssetResponse> filtered = new ArrayList<>();
        for (Document doc : docs) {
            if (role.equals("admin") || doc.get("owner").equals(uid)) {
                filtered.add(Utility.getResponseFromAssetDoc(doc));
            } else {
                filtered.add(Utility.getResponseViewFromAssetDoc(doc));
            }
        }
        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }

    public ResponseEntity<Void> addFavoriteAsset(String assetId, String uid) {
        Document doc;
        Document id;
        try {
            id = new Document("_id", new ObjectId(assetId));
            doc = repository.findOne(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (doc == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<String> users = doc.getList("markedAsFavorite", String.class);
        if (users.contains(uid)) {
            Document user = new Document("markedAsFavorite", uid);
            Document update = new Document("$pull", user);
            Document fav = repository.findAndUpdate(id, update);
            if (fav == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            Document user = new Document("markedAsFavorite", uid);
            Document update = new Document("$addToSet", user);
            Document fav = repository.findAndUpdate(id, update);
            if (fav == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<List<AssetResponse>> getFavoriteAssets(String uid, String role) {
        Document filter = new Document();

        List<String> ids = new ArrayList<>();
        ids.add(uid);
        filter.append("markedAsFavorite", new Document("$elemMatch", new Document("$in", ids)));

        FindIterable<Document> docs = repository.findByVisited(filter);

        List<AssetResponse> filtered = new ArrayList<>();
        for (Document doc : docs) {
            if (role.equals("admin") || doc.get("owner").equals(uid)) {
                filtered.add(Utility.getResponseFromAssetDoc(doc));
            } else {
                filtered.add(Utility.getResponseViewFromAssetDoc(doc));
            }
        }
        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }
}
