package edu.bbte.projectbluebook.datacatalog.assets;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DeleteTagTests {
    @Autowired
    private AssetMongoService service;

    @MockBean
    private AssetMongoRepository repository;

    @Test
    public void testDeleteTag1() {
        // Wrong mongo id format
        String id = "123";
        /*assertEquals("Wrong id format.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.deleteTag("something", id));*/
    }

    @Test
    public void testDeleteTag2() {
        // No asset with given id
        String id = "5fa7da8d2b647c594788e3c5";
        when(repository.findAndUpdate(any(Document.class), any(Document.class)))
                .thenReturn(null);
       /* assertEquals("No asset with given id.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.deleteTag("something", id));*/
    }

    @Test
    public void testDeleteTag3() {
        // Okay
        String id = "5fa7da8d2b647c594788e3c5";
        when(repository.findAndUpdate(any(Document.class), any(Document.class)))
                .thenReturn(new Document());
        /*assertEquals("Okay.",
                new ResponseEntity<>(HttpStatus.NO_CONTENT),
                service.deleteTag("something", id));*/
    }
}
