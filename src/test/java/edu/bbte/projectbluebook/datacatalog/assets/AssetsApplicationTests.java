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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class AssetsApplicationTests {

    @Autowired
    private AssetMongoService service;

    @MockBean
    private AssetMongoRepository repository;

    @Test
    void contextLoads() {
    }

    @Test
    public void createAsset() {
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

}
