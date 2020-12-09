package edu.bbte.projectbluebook.datacatalog.assets;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
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

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class GetAssetsTests {
    @Autowired
    private AssetMongoService service;

    @MockBean
    private AssetMongoRepository repository;

    @Test
    public void testGetAssets1() {
        // No match
        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);

        Document filter = new Document();
        when(repository.findByVisited(filter)).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(false);
        assertEquals("Empty array.",
                new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK),
                service.getAssets(new ArrayList<>(), ""));
    }

    @Test
    public void testGetAssets2() {
        // With tags and namespace
        List<String> tags = new ArrayList<>();
        tags.add("goat");
        tags.add("goats");
        String namespace = "IRIS";
        Document filter = new Document()
                .append("namespace", namespace)
                .append("tags", new Document("$elemMatch", new Document("$in", tags)));
        Document params = new Document().append("url", "https://something.com");
        Document loc = new Document()
                .append("type", "url")
                .append("parameters", params);
        Document doc1 = new Document()
                .append("_id", new ObjectId("5fa7da8d2b647c494788e3c5"))
                .append("name", "IRIS1")
                .append("description", "desc1")
                .append("namespace", "IRIS")
                .append("size", "50")
                .append("format", "json")
                .append("location", loc)
                .append("visited", Long.valueOf(20))
                .append("createdAt", new Date())
                .append("updatedAt", new Date())
                .append("tags", tags);
        Document doc2 = new Document()
                .append("_id", new ObjectId("5fa7da8d2b647c594788e3c5"))
                .append("name", "IRIS2")
                .append("description", "desc2")
                .append("namespace", "IRIS")
                .append("size", "450")
                .append("format", "csv")
                .append("location", loc)
                .append("visited", Long.valueOf(253))
                .append("createdAt", new Date())
                .append("updatedAt", new Date())
                .append("tags", tags);
        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);
        when(repository.findByVisited(filter)).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(cursor.next())
                .thenReturn(doc1)
                .thenReturn(doc2);
        List<AssetResponse> filtered = new ArrayList<>();
        filtered.add(Utility.getResponseFromAssetDoc(doc1));
        filtered.add(Utility.getResponseFromAssetDoc(doc2));
        assertEquals("2 Matches found.",
                new ResponseEntity<>(filtered, HttpStatus.OK),
                service.getAssets(tags, namespace));
    }

    @Test
    public void testGetAssets3() {
        // No match and tags + namespace null
        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);

        Document filter = new Document();
        when(repository.findByVisited(filter)).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(false);
        assertEquals("Empty array.",
                new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK),
                service.getAssets(null, null));
    }
}
