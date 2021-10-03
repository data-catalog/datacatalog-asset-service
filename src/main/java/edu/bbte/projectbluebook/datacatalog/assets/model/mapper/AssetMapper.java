package edu.bbte.projectbluebook.datacatalog.assets.model.mapper;

import edu.bbte.projectbluebook.datacatalog.assets.model.Asset;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetCreationRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.AssetUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class AssetMapper {
    public abstract Asset creationRequestDtoToModel(AssetCreationRequest assetCreationRequest);

    public abstract AssetResponse modelToResponseDto(Asset asset);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Asset updateModelFromDto(@MappingTarget Asset asset, AssetUpdateRequest assetUpdateRequest);
}
