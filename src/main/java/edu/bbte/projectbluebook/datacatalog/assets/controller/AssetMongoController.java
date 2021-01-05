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
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        return service.createAsset(assetRequest, id);
    }

    @Override
    public ResponseEntity<Void> deleteAsset(String assetId) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();
        return service.deleteAsset(assetId, id, role);
    }

    @Override
    public ResponseEntity<AssetResponse> getAsset(String assetId) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();
        return service.getAsset(assetId, id, role);
    }

    @Override
    public ResponseEntity<List<AssetResponse>> getAssets(@Valid List<String> tags, @Valid String namespace) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();
        return service.getAssets(tags, namespace, id, role);
    }

    @Override
    public ResponseEntity<Void> patchAsset(String assetId, @Valid AssetRequest assetRequest) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();
        return service.patchAsset(assetId, assetRequest, id, role);
    }

    @Override
    public ResponseEntity<Void> addTag(@Size(min = 1) String tag, String assetId) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();
        return service.addTag(tag, assetId, id, role);
    }

    @Override
    public ResponseEntity<Void> deleteTag(@Size(min = 1) String tag, String assetId) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();
        return service.deleteTag(tag, assetId, id, role);
    }

    @Override
    public ResponseEntity<List<AssetResponse>> searchAssets(
            String keyword,
            @Valid List<String> tags,
            @Valid String namespace,
            @Valid String owner) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();
        return service.searchAssets(keyword, tags, namespace, owner, id, role);
    }
}
