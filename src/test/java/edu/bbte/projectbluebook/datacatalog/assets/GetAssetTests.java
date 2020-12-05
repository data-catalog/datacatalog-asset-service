package edu.bbte.projectbluebook.datacatalog.assets;

import edu.bbte.projectbluebook.datacatalog.assets.model.AssetRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.Parameter;
import edu.bbte.projectbluebook.datacatalog.assets.repository.AssetMongoRepository;
import edu.bbte.projectbluebook.datacatalog.assets.service.AssetMongoService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class GetAssetTests {
    @Autowired
    private AssetMongoService service;

    @MockBean
    private AssetMongoRepository repository;

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

    @Test
    public void getAssetTest1() {
        // Invalid mongo id test
        String id = "123";
        assertEquals("Not Mongo id.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.getAsset(id));
    }

    @Test
    public void getAssetTest2() {
        // Asset with given id was not found
        String id = "5fa7da8d2b647c494788e3c5";
        Document docID = new Document("_id", new ObjectId(id));
        Document update = new Document("$inc", new Document("visited", 1));
        when(repository.findAndUpdate(docID, update)).thenReturn(null);
        assertEquals("Asset with given id was not found.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.getAsset(id));
    }

    @Test
    public void getAssetTest3() {
        // Mongo failure mock while update refresh visited field
        String id = "5fa7da8d2b647c494788e3c5";
        Document docID = new Document("_id", new ObjectId(id));
        Document update = new Document("$inc", new Document("visited", 1));
        Document params = new Document().append("url", "https://something.com");
        Document loc = new Document()
                .append("type", "url")
                .append("parameters", params);
        when(repository.findAndUpdate(docID, update)).thenReturn(new Document()
                .append("_id", new ObjectId(id))
                .append("name", "IRIS")
                .append("description", "desc")
                .append("namespace", "IRIS DATASET")
                .append("size", "50")
                .append("format", "json")
                .append("location", loc)
                .append("visited", Long.MAX_VALUE - 2000)
                .append("createdAt", new Date())
                .append("updatedAt", new Date())
                .append("tags", new ArrayList<String>())
        );
        Document updateRefresh = new Document("$inc", new Document("visited", - 10000));
        when(repository.update(docID, updateRefresh)).thenReturn(false);
        assertEquals("Mongo error mocked.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.getAsset(id));
    }

    @Test
    public void getAssetTest4() {
        // Okay, with refresh visited
        String id = "5fa7da8d2b647c494788e3c5";
        Document docID = new Document("_id", new ObjectId(id));
        Document update = new Document("$inc", new Document("visited", 1));
        Document params = new Document().append("url", "https://something.com");
        Document loc = new Document()
                .append("type", "url")
                .append("parameters", params);
        Document doc = new Document()
                .append("_id", new ObjectId(id))
                .append("name", "IRIS")
                .append("description", "desc")
                .append("namespace", "IRIS DATASET")
                .append("size", "50")
                .append("format","json")
                .append("location", loc)
                .append("visited", Long.MAX_VALUE - 2000)
                .append("createdAt", new Date())
                .append("updatedAt", new Date())
                .append("tags", new ArrayList<String>());
        when(repository.findAndUpdate(docID, update)).thenReturn(doc);
        Document updateRefresh = new Document("$inc", new Document("visited", - 10000));
        when(repository.update(docID, updateRefresh)).thenReturn(true);
        AssetResponse assetResponse = getResponseFromAssetDoc(doc);
        assertEquals("Everything okay and visited refreshed successfully",
                new ResponseEntity<>(assetResponse, HttpStatus.OK),
                service.getAsset(id));
    }

    @Test
    public void getAssetTest5() {
        // Okay
        String id = "5fa7da8d2b647c494788e3c5";
        Document docID = new Document("_id", new ObjectId(id));
        Document update = new Document("$inc", new Document("visited", 1));
        Document params = new Document().append("url", "https://something.com");
        Document loc = new Document()
                .append("type", "url")
                .append("parameters", params);
        Document doc = new Document()
                .append("_id", new ObjectId(id))
                .append("name", "IRIS")
                .append("description", "desc")
                .append("namespace", "IRIS DATASET")
                .append("size", "50")
                .append("format", "json")
                .append("location", loc)
                .append("visited", Long.valueOf(20))
                .append("createdAt", new Date())
                .append("updatedAt", new Date())
                .append("tags", new ArrayList<String>());
        when(repository.findAndUpdate(docID, update)).thenReturn(doc);
        AssetResponse assetResponse = getResponseFromAssetDoc(doc);
        assertEquals("Everything okay and visited refreshed successfully",
                new ResponseEntity<>(assetResponse, HttpStatus.OK),
                service.getAsset(id));
    }
}
