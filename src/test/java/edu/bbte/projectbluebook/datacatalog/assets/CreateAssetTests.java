package edu.bbte.projectbluebook.datacatalog.assets;

import edu.bbte.projectbluebook.datacatalog.assets.model.AssetRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.Parameter;
import edu.bbte.projectbluebook.datacatalog.assets.repository.AssetMongoRepository;
import edu.bbte.projectbluebook.datacatalog.assets.service.AssetMongoService;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class CreateAssetTests {
    @Autowired
    private AssetMongoService service;

    @MockBean
    private AssetMongoRepository repository;

    @Test
    public void testCreateAsset1() {
        // Everything is okay
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setName("IRIS2");
        assetRequest.setDescription("desc");
        assetRequest.setNamespace("IRIS DATASET");
        assetRequest.setSize("50");
        assetRequest.setFormat(AssetRequest.FormatEnum.JSON);
        Location loc = new Location();
        loc.setType("url");
        List<Parameter> params = new ArrayList<>();
        Parameter param = new Parameter();
        param.setKey("url");
        param.setValue("https://something.com");
        params.add(param);
        loc.setParameters(params);
        assetRequest.setLocation(loc);
        Document parameters = new Document();
        Document location = new Document();
        Location assetLocation = assetRequest.getLocation();
        assetLocation
                .getParameters()
                .stream()
                .forEach(item -> parameters.append(item.getKey(), item.getValue()));
        Document asset = new Document();
        location.append("type", assetRequest.getLocation().getType());
        location.append("parameters", parameters);
        asset.append("name", assetRequest.getName());
        asset.append("description", assetRequest.getDescription());
        asset.append("location", location);
        asset.append("tags", assetRequest.getTags());
        asset.append("format", assetRequest.getFormat().getValue());
        asset.append("size", Double.valueOf(50));
        asset.append("namespace", assetRequest.getNamespace());
        asset.append("visited", Long.valueOf(0));
        when(repository.insert(asset)).thenReturn(true);
        assertEquals("Asset was successfully created.",
                new ResponseEntity<>(HttpStatus.CREATED),
                service.createAsset(assetRequest));
    }

    @Test
    public void testCreateAsset2() {
        // Blank name
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setName("");
        assetRequest.setDescription("desc");
        assetRequest.setNamespace("IRIS DATASET");
        assetRequest.setSize("50");
        assetRequest.setFormat(AssetRequest.FormatEnum.JSON);
        Location loc = new Location();
        loc.setType("url");
        List<Parameter> params = new ArrayList<>();
        Parameter param = new Parameter();
        param.setKey("url");
        param.setValue("https://something.com");
        params.add(param);
        loc.setParameters(params);
        assetRequest.setLocation(loc);
        try {
            service.createAsset(assetRequest);
        } catch (ResponseStatusException e) {
            if (!e.getMessage().equals("422 UNPROCESSABLE_ENTITY \"Assets must have a name!\"")) {
                //System.out.println("[" + e.getMessage() + "]");
                throw new AssertionError("Blank name. TEST FAILED");
            }
        }
    }

    @Test
    public void testCreateAsset3() {
        // Blank description
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setName("IRIS2");
        assetRequest.setDescription("");
        assetRequest.setNamespace("IRIS DATASET");
        assetRequest.setSize("50");
        assetRequest.setFormat(AssetRequest.FormatEnum.JSON);
        Location loc = new Location();
        loc.setType("url");
        List<Parameter> params = new ArrayList<>();
        Parameter param = new Parameter();
        param.setKey("url");
        param.setValue("https://something.com");
        params.add(param);
        loc.setParameters(params);
        assetRequest.setLocation(loc);
        try {
            service.createAsset(assetRequest);
        } catch (ResponseStatusException e) {
            if (!e.getMessage().equals("422 UNPROCESSABLE_ENTITY \"Assets must have a description!\"")) {
                //System.out.println("[" + e.getMessage() + "]");
                throw new AssertionError("Blank description error. TEST FAILED");
            }
        }
    }

    @Test
    public void testCreateAsset4() {
        // size format error
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setName("IRIS2");
        assetRequest.setDescription("desc");
        assetRequest.setNamespace("IRIS DATASET");
        assetRequest.setSize("50MB");
        assetRequest.setFormat(AssetRequest.FormatEnum.JSON);
        Location loc = new Location();
        loc.setType("url");
        List<Parameter> params = new ArrayList<>();
        Parameter param = new Parameter();
        param.setKey("url");
        param.setValue("https://something.com");
        params.add(param);
        loc.setParameters(params);
        assetRequest.setLocation(loc);
        try {
            service.createAsset(assetRequest);
        } catch (ResponseStatusException e) {
            if (!e.getMessage().equals("422 UNPROCESSABLE_ENTITY \"Size must be a positive number (size in MB).\"")) {
                //System.out.println("[" + e.getMessage() + "]");
                throw new AssertionError("Size error. TEST FAILED");
            }
        }
    }

    @Test
    public void testCreateAsset5() {
        // Mocking a database error
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setName("IRIS2");
        assetRequest.setDescription("desc");
        assetRequest.setNamespace("IRIS DATASET");
        assetRequest.setSize("50");
        assetRequest.setFormat(AssetRequest.FormatEnum.JSON);
        Location loc = new Location();
        loc.setType("url");
        List<Parameter> params = new ArrayList<>();
        Parameter param = new Parameter();
        param.setKey("url");
        param.setValue("https://something.com");
        params.add(param);
        loc.setParameters(params);
        assetRequest.setLocation(loc);
        Document parameters = new Document();
        Document location = new Document();
        Location assetLocation = assetRequest.getLocation();
        assetLocation
                .getParameters()
                .stream()
                .forEach(item -> parameters.append(item.getKey(), item.getValue()));
        Document asset = new Document();
        location.append("type", assetRequest.getLocation().getType());
        location.append("parameters", parameters);
        asset.append("name", assetRequest.getName());
        asset.append("description", assetRequest.getDescription());
        asset.append("location", location);
        asset.append("tags", assetRequest.getTags());
        asset.append("format", assetRequest.getFormat().getValue());
        asset.append("size", Double.valueOf(50));
        asset.append("namespace", assetRequest.getNamespace());
        asset.append("visited", Long.valueOf(0));
        when(repository.insert(asset)).thenReturn(false);
        assertEquals("Database error mocked.",
                new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY),
                service.createAsset(assetRequest));
    }
}
