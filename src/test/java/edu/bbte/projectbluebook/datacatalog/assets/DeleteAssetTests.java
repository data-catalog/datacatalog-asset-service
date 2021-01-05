package edu.bbte.projectbluebook.datacatalog.assets;

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

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class DeleteAssetTests {
    @Autowired
    private AssetMongoService service;

    @MockBean
    private AssetMongoRepository repository;

    @Test
    public void testDeleteAsset1() {
        // Invalid mongo id test
        String id = "123";
        /*assertEquals("Not Mongo id.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.deleteAsset(id));*/
    }

    @Test
    public void testDeleteAsset2() {
        // Okay
        String id = "5fa7da8d2b647c494788e3c5";
        when(repository.delete(new Document("_id", new ObjectId(id)))).thenReturn(new Document());
       /* assertEquals("Asset successfully deleted.",
                new ResponseEntity<>(HttpStatus.NO_CONTENT),
                service.deleteAsset(id));*/
    }

    @Test
    public void testDeleteAsset3() {
        // No asset with given id was present in the first place
        String id = "5fa7da8d2b647c494788e3c5";
        when(repository.delete(new Document("_id", new ObjectId(id)))).thenReturn(null);
        /*assertEquals("Asset didnt exist in the first place.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.deleteAsset(id));*/
    }
}
