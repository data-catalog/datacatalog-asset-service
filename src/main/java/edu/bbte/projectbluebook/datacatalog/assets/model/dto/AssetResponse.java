package edu.bbte.projectbluebook.datacatalog.assets.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.LocationResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * The model of a data asset which is sent from the server to the client.
 */
@ApiModel(description = "The model of a data asset which is sent from the server to the client.")

public class AssetResponse  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private String id;

  @JsonProperty("createdAt")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @JsonProperty("updatedAt")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  @JsonProperty("ownerId")
  private String ownerId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("shortDescription")
  private String shortDescription;

  @JsonProperty("location")
  private LocationResponse location;

  @JsonProperty("tags")
  @Valid
  private List<String> tags = null;

  /**
   * The file format of the asset. 
   */
  public enum FormatEnum {
    CSV("csv"),
    
    JSON("json");

    private String value;

    FormatEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static FormatEnum fromValue(String value) {
      for (FormatEnum b : FormatEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("format")
  private FormatEnum format;

  @JsonProperty("namespace")
  private String namespace;

  @JsonProperty("isPublic")
  private Boolean isPublic;

  @JsonProperty("members")
  @Valid
  private List<String> members = null;

  public AssetResponse id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Unique identifier of the asset.
   * @return id
  */
  @ApiModelProperty(value = "Unique identifier of the asset.")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AssetResponse createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Date of creation.
   * @return createdAt
  */
  @ApiModelProperty(value = "Date of creation.")

  @Valid

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public AssetResponse updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Date of the last update.
   * @return updatedAt
  */
  @ApiModelProperty(value = "Date of the last update.")

  @Valid

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public AssetResponse ownerId(String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * The id of the user who owns the Asset.
   * @return ownerId
  */
  @ApiModelProperty(value = "The id of the user who owns the Asset.")


  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public AssetResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * A short name of the data asset.
   * @return name
  */
  @ApiModelProperty(example = "Iris Dataset", value = "A short name of the data asset.")

@Size(min=3,max=120) 
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AssetResponse description(String description) {
    this.description = description;
    return this;
  }

  /**
   * A longer description about the content of the data asset.
   * @return description
  */
  @ApiModelProperty(example = "This is perhaps the best known database to be found in the pattern recognition literature. Fisher's paper is a classic in the field and is referenced frequently to this day. (See Duda & Hart, for example.) The data set contains 3 classes of 50 instances each, where each class refers to a type of iris plant. One class is linearly separable from the other 2; the latter are NOT linearly separable from each other.", value = "A longer description about the content of the data asset.")

@Size(min=3) 
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AssetResponse shortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
    return this;
  }

  /**
   * Short description breifly definig an asset.
   * @return shortDescription
  */
  @ApiModelProperty(example = "This is perhaps the best known database to be found in the pattern recognition literature.", value = "Short description breifly definig an asset.")

@Size(max=300) 
  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public AssetResponse location(LocationResponse location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
  */
  @ApiModelProperty(value = "")

  @Valid

  public LocationResponse getLocation() {
    return location;
  }

  public void setLocation(LocationResponse location) {
    this.location = location;
  }

  public AssetResponse tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public AssetResponse addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Keywords assigned to the asset.
   * @return tags
  */
  @ApiModelProperty(value = "Keywords assigned to the asset.")


  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public AssetResponse format(FormatEnum format) {
    this.format = format;
    return this;
  }

  /**
   * The file format of the asset. 
   * @return format
  */
  @ApiModelProperty(example = "csv", value = "The file format of the asset. ")


  public FormatEnum getFormat() {
    return format;
  }

  public void setFormat(FormatEnum format) {
    this.format = format;
  }

  public AssetResponse namespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * The namespace of the asset. An asset has one namespace, which can be used to group assets together (eg. by projects).
   * @return namespace
  */
  @ApiModelProperty(example = "flowerproject", value = "The namespace of the asset. An asset has one namespace, which can be used to group assets together (eg. by projects).")


  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public AssetResponse isPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * Whether the asset is accessible by anyone or not.
   * @return isPublic
  */
  @ApiModelProperty(value = "Whether the asset is accessible by anyone or not.")


  public Boolean getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  public AssetResponse members(List<String> members) {
    this.members = members;
    return this;
  }

  public AssetResponse addMembersItem(String membersItem) {
    if (this.members == null) {
      this.members = new ArrayList<>();
    }
    this.members.add(membersItem);
    return this;
  }

  /**
   * The ID-s of the users which the asset is accessible for.
   * @return members
  */
  @ApiModelProperty(value = "The ID-s of the users which the asset is accessible for.")


  public List<String> getMembers() {
    return members;
  }

  public void setMembers(List<String> members) {
    this.members = members;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssetResponse assetResponse = (AssetResponse) o;
    return Objects.equals(this.id, assetResponse.id) &&
        Objects.equals(this.createdAt, assetResponse.createdAt) &&
        Objects.equals(this.updatedAt, assetResponse.updatedAt) &&
        Objects.equals(this.ownerId, assetResponse.ownerId) &&
        Objects.equals(this.name, assetResponse.name) &&
        Objects.equals(this.description, assetResponse.description) &&
        Objects.equals(this.shortDescription, assetResponse.shortDescription) &&
        Objects.equals(this.location, assetResponse.location) &&
        Objects.equals(this.tags, assetResponse.tags) &&
        Objects.equals(this.format, assetResponse.format) &&
        Objects.equals(this.namespace, assetResponse.namespace) &&
        Objects.equals(this.isPublic, assetResponse.isPublic) &&
        Objects.equals(this.members, assetResponse.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdAt, updatedAt, ownerId, name, description, shortDescription, location, tags, format, namespace, isPublic, members);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssetResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    shortDescription: ").append(toIndentedString(shortDescription)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    namespace: ").append(toIndentedString(namespace)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

