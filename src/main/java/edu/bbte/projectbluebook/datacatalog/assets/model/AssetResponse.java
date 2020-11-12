package edu.bbte.projectbluebook.datacatalog.assets.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import edu.bbte.projectbluebook.datacatalog.assets.model.AssetRequest;
import edu.bbte.projectbluebook.datacatalog.assets.model.AssetResponseAllOf;
import edu.bbte.projectbluebook.datacatalog.assets.model.Location;
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

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("location")
  private Location location;

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

  @JsonProperty("size")
  private String size;

  @JsonProperty("namespace")
  private String namespace;

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

  public AssetResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * A short name of the data asset.
   * @return name
  */
  @ApiModelProperty(example = "Iris Dataset", value = "A short name of the data asset.")


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


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AssetResponse location(Location location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
  */
  @ApiModelProperty(value = "")

  @Valid

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
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

  public AssetResponse size(String size) {
    this.size = size;
    return this;
  }

  /**
   * The approximate size of the asset.
   * @return size
  */
  @ApiModelProperty(value = "The approximate size of the asset.")


  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
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
        Objects.equals(this.name, assetResponse.name) &&
        Objects.equals(this.description, assetResponse.description) &&
        Objects.equals(this.location, assetResponse.location) &&
        Objects.equals(this.tags, assetResponse.tags) &&
        Objects.equals(this.format, assetResponse.format) &&
        Objects.equals(this.size, assetResponse.size) &&
        Objects.equals(this.namespace, assetResponse.namespace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdAt, updatedAt, name, description, location, tags, format, size, namespace);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssetResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    namespace: ").append(toIndentedString(namespace)).append("\n");
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

