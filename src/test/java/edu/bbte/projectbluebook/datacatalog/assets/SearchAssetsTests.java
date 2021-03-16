package edu.bbte.projectbluebook.datacatalog.assets;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import edu.bbte.projectbluebook.datacatalog.assets.helpers.Utility;
import edu.bbte.projectbluebook.datacatalog.assets.model.AssetResponse;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SearchAssetsTests {
    @Autowired
    private AssetMongoService service;

    @MockBean
    private AssetMongoRepository repository;

    @Test
    public void testSearchAssets1() {
        // no match + keyword + no others
        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);
        when(repository.findByVisited(any(Document.class))).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext())
                .thenReturn(false);
        String keyword = "sOmEtHiNg";
       /* assertEquals("OK + Empty array.",
                new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK),
                service.searchAssets(keyword, null, null));*/
    }

    @Test
    public void testSearchAssets2() {
        // match + no keyword + namespace + tags
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
                .append("tags", new ArrayList<>());
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
                .append("tags", new ArrayList<>());
        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);
        when(repository.findByVisited(any(Document.class))).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(cursor.next())
                .thenReturn(doc1)
                .thenReturn(doc2);
        List<String> tags = new ArrayList<>();
        tags.add("goat");
        tags.add("something");
        List<AssetResponse> filtered = new ArrayList<>();
        filtered.add(Utility.getResponseFromAssetDoc(doc1));
        filtered.add(Utility.getResponseFromAssetDoc(doc2));
        String namespace = "IRIS";
        /*assertEquals("OK.",
                new ResponseEntity<>(filtered, HttpStatus.OK),
                service.searchAssets(null, tags, namespace));*/
    }

    @Test
    public void testSearchAssets3() {
        // parameters blank
        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);
        when(repository.findByVisited(any(Document.class))).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext())
                .thenReturn(false);
        String keyword = "        ";
        String namespace = "    ";
        List<String> tags = new ArrayList<>();
      /*  assertEquals("OK + Empty array + blank inputs.",
                new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK),
                service.searchAssets(keyword, tags, namespace));*/
    }
}
