package edu.bbte.projectbluebook.datacatalog.assets.controller;

import edu.bbte.projectbluebook.datacatalog.assets.api.AssetApi;
import edu.bbte.projectbluebook.datacatalog.assets.model.*;
import edu.bbte.projectbluebook.datacatalog.assets.service.AssetMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.*;

@RestController
public class AssetMongoController implements AssetApi  {

    @Autowired
    private AssetMongoService service;

    @Override
    public ResponseEntity<Void> createAsset(@Valid AssetRequest assetRequest) {
        return service.createAsset(assetRequest);
    }

    @Override
    public ResponseEntity<Void> deleteAsset(String assetId) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();
        return service.deleteAsset(assetId);
    }

    @Override
    public ResponseEntity<AssetResponse> getAsset(String assetId) {
        return service.getAsset(assetId);
    }

    @Override
    public ResponseEntity<List<AssetResponse>> getAssets(@Valid List<String> tags, @Valid String namespace) {
        return service.getAssets(tags, namespace);
    }

    @Override
    public ResponseEntity<Void> patchAsset(String assetId, @Valid AssetRequest assetRequest) {
        return service.patchAsset(assetId, assetRequest);
    }

    @Override
    public ResponseEntity<Void> addTag(@Size(min = 1) String tag, String assetId) {
        return service.addTag(tag, assetId);
    }

    @Override
    public ResponseEntity<Void> deleteTag(@Size(min = 1) String tag, String assetId) {
        return service.deleteTag(tag, assetId);
    }

    @Override
    public ResponseEntity<List<AssetResponse>> searchAssets(
            String keyword,
            @Valid List<String> tags,
            @Valid String namespace) {
        return service.searchAssets(keyword, tags, namespace);
    }
}
