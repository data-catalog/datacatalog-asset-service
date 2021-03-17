package edu.bbte.projectbluebook.datacatalog.assets.controller;

import edu.bbte.projectbluebook.datacatalog.assets.api.AssetApi;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetCreationRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetUpdateRequest;
import edu.bbte.projectbluebook.datacatalog.assets.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.List;

@Controller
public class AssetController implements AssetApi {
    @Autowired
    private AssetService service;

    @Override
    public Mono<ResponseEntity<Void>> addTag(@Size(min = 1) String tag, String assetId, ServerWebExchange exchange) {
        return service
                .addTag(assetId, tag)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> createAsset(@Valid Mono<AssetCreationRequest> assetCreationRequest,
                                                  ServerWebExchange exchange) {
        // FIXME: Extract location and return 201
        Mono<String> ownerId = exchange.getPrincipal().map(Principal::getName);
        return service
                .createAsset(assetCreationRequest, ownerId)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteAsset(String assetId, ServerWebExchange exchange) {
        return service
                .deleteAsset(assetId)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteTag(@Size(min = 1) String tag, String assetId, ServerWebExchange exchange) {
        return service
                .removeTag(assetId, tag)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AssetResponse>> getAsset(String assetId, ServerWebExchange exchange) {
        return service
                .getAsset(assetId)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<AssetResponse>>> getAssets(@Valid List<String> tags, @Valid String namespace,
                                                               ServerWebExchange exchange) {
        return Mono
                .just(service.getAssets())
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> patchAsset(String assetId, @Valid Mono<AssetUpdateRequest> assetUpdateRequest,
                                                 ServerWebExchange exchange) {
        return service
                .updateAsset(assetId, assetUpdateRequest)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<AssetResponse>>> searchAssets(String keyword, @Valid List<String> tags,
                                                                  @Valid String namespace, @Valid String owner,
                                                                  ServerWebExchange exchange) {
        return Mono
                .just(service.searchAssets(keyword))
                .map(ResponseEntity::ok);
    }

    // TODO: implement addFavoriteAsset, getFavoriteAssets endpoints
}
