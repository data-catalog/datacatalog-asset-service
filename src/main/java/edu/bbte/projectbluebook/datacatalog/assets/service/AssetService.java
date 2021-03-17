package edu.bbte.projectbluebook.datacatalog.assets.service;

import edu.bbte.projectbluebook.datacatalog.assets.exception.AssetServiceException;
import edu.bbte.projectbluebook.datacatalog.assets.exception.NotFoundException;
import edu.bbte.projectbluebook.datacatalog.assets.exception.ValidationException;
import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetCreationRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetUpdateRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.mapper.AssetMapper;
import edu.bbte.projectbluebook.datacatalog.assets.repository.AssetRepository;
import edu.bbte.projectbluebook.datacatalog.assets.util.LocationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AssetService {
    @Autowired
    private AssetRepository repository;

    @Autowired
    private AssetMapper mapper;

    @Autowired
    private LocationValidator locationValidator;

    public Mono<Void> createAsset(Mono<AssetCreationRequest> assetRequest, Mono<String> ownerId) {
        return assetRequest
                .map(mapper::creationRequestDtoToModel)
                .flatMap(asset -> ownerId.map(asset::setOwnerId))
                .map(asset -> {
                    Location validatedLocation = locationValidator.validateLocation(asset.getLocation());

                    return asset.setLocation(validatedLocation);
                })
                .flatMap(asset -> repository.insert(asset))
                .then()
                .onErrorMap(
                        ex -> !(ex instanceof ValidationException),
                        err -> new AssetServiceException("Asset could not be created.")
                );
    }

    public Mono<Void> deleteAsset(String assetId) {
        // TODO: delete associated versions
        return repository
                .deleteById(assetId)
                .onErrorMap(err -> new AssetServiceException("Asset could not be deleted."));
    }

    public Mono<AssetResponse> getAsset(String assetId) {
        return repository
                .findById(assetId)
                .map(mapper::modelToResponseDto)
                .onErrorMap(err -> new AssetServiceException("Asset could not be retrieved."))
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")));
    }

    public Flux<AssetResponse> getAssets() {
        return repository
                .findAll()
                .map(mapper::modelToResponseDto)
                .onErrorMap(err -> new AssetServiceException("Assets could not be retrieved."));
    }

    public Mono<Void> updateAsset(String assetId, Mono<AssetUpdateRequest> assetRequest) {
        return repository
                .findById(assetId)
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")))
                .zipWith(assetRequest)
                .map(tuple -> mapper.updateModelFromDto(tuple.getT1(), tuple.getT2()))
                .flatMap(repository::save)
                .then()
                .onErrorMap(err -> new AssetServiceException("Asset could not be updated."));
    }

    public Mono<Void> addTag(String assetId, String tag) {
        return repository
                .findById(assetId)
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")))
                .map(asset -> {
                    if(!asset.getTags().contains(tag)) {
                        asset.getTags().add(tag);
                    }
                    return asset;
                })
                .flatMap(repository::save)
                .then()
                .onErrorMap(err -> new AssetServiceException("Tag could not be added."));
    }

    public Mono<Void> removeTag(String assetId, String tag) {
        return repository
                .findById(assetId)
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")))
                .map(asset -> {
                    asset.getTags().remove(tag);
                    return asset;
                })
                .flatMap(repository::save)
                .then()
                .onErrorMap(err -> new AssetServiceException("Tag could not be added."));
    }

    public Flux<AssetResponse> searchAssets(String name) {
        // TODO: include other search criterion
        return repository
                .findAllByNameContainingIgnoreCase(name)
                .map(mapper::modelToResponseDto)
                .onErrorMap(err -> new AssetServiceException("Assets could not be retrieved."));
    }

    // TODO: implement searchAssets, addFavoriteAsset and getFavoriteAssets
}
