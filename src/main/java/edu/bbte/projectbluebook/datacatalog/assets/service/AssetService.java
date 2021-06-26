package edu.bbte.projectbluebook.datacatalog.assets.service;

import edu.bbte.projectbluebook.datacatalog.assets.exception.NotFoundException;
import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetCreationRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetUpdateRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.mapper.AssetMapper;
import edu.bbte.projectbluebook.datacatalog.assets.repository.AssetRepository;
import edu.bbte.projectbluebook.datacatalog.assets.util.LocationProcessor;
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
    private LocationProcessor locationProcessor;

    public Mono<Void> createAsset(Mono<AssetCreationRequest> assetRequest, Mono<String> ownerId) {
        return assetRequest
                .map(mapper::creationRequestDtoToModel)
                .flatMap(asset -> ownerId.map(asset::setOwnerId))
                .map(asset -> {
                    Location validatedLocation = locationProcessor.validateLocation(asset.getLocation());
                    locationProcessor.encryptTokens(validatedLocation);
                    return asset.setLocation(validatedLocation);
                })
                .flatMap(asset -> repository.insert(asset))
                .then();
    }

    public Mono<Void> deleteAsset(String assetId) {
        return repository
                .deleteById(assetId);
    }

    public Mono<AssetResponse> getAsset(String assetId) {
        return repository
                .findById(assetId)
                .map(asset -> asset.setLocation(locationProcessor.decryptTokens(asset.getLocation())))
                .map(mapper::modelToResponseDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")));
    }

    public Flux<AssetResponse> getAssets(String userId) {
        return repository
                .findAll()
                .filter(asset -> asset.getIsPublic()
                        || asset.getMembers().contains(userId)
                        || asset.getOwnerId().equals(userId))
                .map(asset -> asset.setLocation(locationProcessor.decryptTokens(asset.getLocation())))
                .map(mapper::modelToResponseDto);
    }

    public Mono<Void> updateAsset(String assetId, Mono<AssetUpdateRequest> assetRequest) {
        return repository
                .findById(assetId)
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")))
                .zipWith(assetRequest)
                .map(tuple -> mapper.updateModelFromDto(tuple.getT1(), tuple.getT2()))
                .map(asset -> {
                    if (asset.getLocation() != null) {
                        Location validatedLocation = locationProcessor.validateLocation(asset.getLocation());
                        locationProcessor.encryptTokens(validatedLocation);
                        asset.setLocation(validatedLocation);
                    }

                    return asset;
                })
                .flatMap(repository::save)
                .then();
    }

    public Mono<Void> addTag(String assetId, String tag) {
        return repository
                .findById(assetId)
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")))
                .map(asset -> {
                    if (!asset.getTags().contains(tag)) {
                        asset.getTags().add(tag);
                    }
                    return asset;
                })
                .flatMap(repository::save)
                .then();
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
                .then();
    }

    public Flux<AssetResponse> searchAssets(String userId, String name) {
        // TODO: include other search criterion
        return repository
                .findAllByNameContainingIgnoreCase(name)
                .filter(asset -> asset.getIsPublic()
                        || asset.getMembers().contains(userId)
                        || asset.getOwnerId().equals(userId))
                .map(asset -> asset.setLocation(locationProcessor.decryptTokens(asset.getLocation())))
                .map(mapper::modelToResponseDto);
    }

    public Flux<AssetResponse> getUserAssets(String userId) {
        return repository
                .findAll()
                .filter(asset -> asset.getMembers().contains(userId)
                        || asset.getOwnerId().equals(userId))
                .map(asset -> asset.setLocation(locationProcessor.decryptTokens(asset.getLocation())))
                .map(mapper::modelToResponseDto);
    }

    // TODO: implement searchAssets, addFavoriteAsset and getFavoriteAssets
}
