package edu.bbte.projectbluebook.datacatalog.assets;

import edu.bbte.projectbluebook.datacatalog.assets.helpers.Utility;
import edu.bbte.projectbluebook.datacatalog.assets.model.AssetResponse;
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

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class GetAssetTests {
    @Autowired
    private AssetMongoService service;

    @MockBean
    private AssetMongoRepository repository;

    @Test
    public void testGetAsset1() {
        // Invalid mongo id test
        String id = "123";
        assertEquals("Not Mongo id.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.getAsset(id));
    }

    @Test
    public void testGetAsset2() {
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
    public void testGetAsset3() {
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
    public void testGetAsset4() {
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
        AssetResponse assetResponse = Utility.getResponseFromAssetDoc(doc);
        assertEquals("Everything okay and visited refreshed successfully",
                new ResponseEntity<>(assetResponse, HttpStatus.OK),
                service.getAsset(id));
    }

    @Test
    public void testGetAsset5() {
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
        AssetResponse assetResponse = Utility.getResponseFromAssetDoc(doc);
        assertEquals("Everything okay and visited refreshed successfully",
                new ResponseEntity<>(assetResponse, HttpStatus.OK),
                service.getAsset(id));
    }
}
