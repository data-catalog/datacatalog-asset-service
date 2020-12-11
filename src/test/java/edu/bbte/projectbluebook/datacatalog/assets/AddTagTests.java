package edu.bbte.projectbluebook.datacatalog.assets;

import edu.bbte.projectbluebook.datacatalog.assets.repository.AssetMongoRepository;
import edu.bbte.projectbluebook.datacatalog.assets.service.AssetMongoService;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class AddTagTests {
    @Autowired
    private AssetMongoService service;

    @MockBean
    private AssetMongoRepository repository;

    @Test
    public void testAddTag1() {
        // Wrong mongo id format
        String id = "123";
        assertEquals("Wrong id format.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.addTag("something", id));
    }

    @Test
    public void testAddTag2() {
        // No asset with given id
        String id = "5fa7da8d2b647c594788e3c5";
        when(repository.findAndUpdate(any(Document.class), any(Document.class)))
                .thenReturn(null);
        assertEquals("No asset with given id.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.addTag("something", id));
    }

    @Test
    public void testAddTag3() {
        // Okay
        String id = "5fa7da8d2b647c594788e3c5";
        when(repository.findAndUpdate(any(Document.class), any(Document.class)))
                .thenReturn(new Document());
        assertEquals("Okay.",
                new ResponseEntity<>(HttpStatus.NO_CONTENT),
                service.addTag("something", id));
    }
}
