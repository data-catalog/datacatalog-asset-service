package edu.bbte.projectbluebook.datacatalog.assets;

import edu.bbte.projectbluebook.datacatalog.assets.model.AssetRequest;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class PatchAssetTests {
    @Autowired
    private AssetMongoService service;

    @MockBean
    private AssetMongoRepository repository;

    @Test
    public void testPatchAsset1() {
        // Mongo id format wrong
        assertEquals("Mongo id format error.",
                new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY),
                service.patchAsset("123", null));
    }

    @Test
    public void testPatchAsset2() {
        // Blank name given
        String id = "5fa7da8d2b647c594788e3c5";
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setName("   ");
        try {
            service.patchAsset(id, assetRequest);
        } catch (ResponseStatusException e) {
            if (!e.getMessage().equals("422 UNPROCESSABLE_ENTITY \"Assets must have a name!\"")) {
                //System.out.println("[" + e.getMessage() + "]");
                throw new AssertionError("Name is given and its empty. TEST FAILED");
            }
        }
    }

    @Test
    public void testPatchAsset3() {
        // Blank description given
        String id = "5fa7da8d2b647c594788e3c5";
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setName("IRIS3");
        assetRequest.setDescription("    ");
        try {
            service.patchAsset(id, assetRequest);
        } catch (ResponseStatusException e) {
            if (!e.getMessage().equals("422 UNPROCESSABLE_ENTITY \"Assets must have a description!\"")) {
                //System.out.println("[" + e.getMessage() + "]");
                throw new AssertionError("Description is given and its empty. TEST FAILED");
            }
        }
    }

    @Test
    public void testPatchAsset4() {
        // Incorrect size format test (not number)
        String id = "5fa7da8d2b647c594788e3c5";
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setSize("MVP");
        try {
            service.patchAsset(id, assetRequest);
        } catch (ResponseStatusException e) {
            if (!e.getMessage().equals("422 UNPROCESSABLE_ENTITY \"Size must be a positive number (size in MB).\"")) {
                //System.out.println("[" + e.getMessage() + "]");
                throw new AssertionError("Size format error. TEST FAILED");
            }
        }
    }

    @Test
    public void testPatchAsset5() {
        // Incorrect size format test (not number)
        String id = "5fa7da8d2b647c594788e3c5";
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setSize("-500");
        try {
            service.patchAsset(id, assetRequest);
        } catch (ResponseStatusException e) {
            if (!e.getMessage().equals("422 UNPROCESSABLE_ENTITY \"Size must be a positive number (size in MB).\"")) {
                //System.out.println("[" + e.getMessage() + "]");
                throw new AssertionError("Size format error. TEST FAILED");
            }
        }
    }

    @Test
    public void testPatchAsset6() {
        // Given location without parameters
        String id = "5fa7da8d2b647c594788e3c5";
        AssetRequest assetRequest = new AssetRequest();
        Location loc = new Location();
        loc.setParameters(new ArrayList<>());
        loc.setType("url");
        assetRequest.setLocation(loc);
        try {
            service.patchAsset(id, assetRequest);
        } catch (ResponseStatusException e) {
            if (!e.getMessage().equals("422 UNPROCESSABLE_ENTITY \"Location object must have parameters!\"")) {
                //System.out.println("[" + e.getMessage() + "]");
                throw new AssertionError("Location is given but does not have parameters. TEST FAILED");
            }
        }
    }

    @Test
    public void testPatchAsset7() {
        // Given location with blank type
        String id = "5fa7da8d2b647c594788e3c5";
        List<Parameter> params = new ArrayList<>();
        Parameter param = new Parameter();
        param.setKey("url");
        param.setValue("https://something.com");
        params.add(param);
        Location loc = new Location();
        loc.setParameters(params);
        loc.setType("      ");
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setLocation(loc);
        try {
            service.patchAsset(id, assetRequest);
        } catch (ResponseStatusException e) {
            if (!e.getMessage().equals("422 UNPROCESSABLE_ENTITY \"Location object must have a type!\"")) {
                //System.out.println("[" + e.getMessage() + "]");
                throw new AssertionError("Location is given but has blank type. TEST FAILED");
            }
        }
    }

    @Test
    public void testPatchAsset8() {
        // No asset with given id existed in the first place + empty tags list
        String id = "5fa7da8d2b647c594788e3c5";
        AssetRequest assetRequest = new AssetRequest();
        Document filter = new Document("_id", new ObjectId(id));
        Document set = new Document();
        set.append("tags", new ArrayList<>());
        assetRequest.setTags(new ArrayList<>());
        when(repository.findAndUpdateMark(filter, new Document("$set", set))).thenReturn(null);
        assertEquals("No asset with given id.",
                new ResponseEntity<>(HttpStatus.NOT_FOUND),
                service.patchAsset(id, assetRequest));
    }

    @Test
    public void testPatchAsset9() {
        // Okay
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setName("IRIS2");
        assetRequest.setDescription("desc2");
        assetRequest.setFormat(AssetRequest.FormatEnum.CSV);
        assetRequest.setNamespace("IRIS");
        assetRequest.setSize("500");
        List<String> tags = new ArrayList<>();
        tags.add("goat");
        assetRequest.setTags(tags);
        List<Parameter> params = new ArrayList<>();
        Parameter param = new Parameter();
        param.setKey("url");
        param.setValue("https://something.com");
        params.add(param);
        Location loc = new Location();
        loc.setParameters(params);
        loc.setType("url");
        assetRequest.setLocation(loc);
        String id = "5fa7da8d2b647c594788e3c5";
        when(repository.findAndUpdateMark(any(Document.class), any(Document.class))).thenReturn(new Document());
        assertEquals("Patch successful.",
                new ResponseEntity<>(HttpStatus.NO_CONTENT),
                service.patchAsset(id, assetRequest));
    }
}
