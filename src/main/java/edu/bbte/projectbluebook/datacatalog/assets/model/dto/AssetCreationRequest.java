package edu.bbte.projectbluebook.datacatalog.assets.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.LocationCreationRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AssetCreationRequest
 */

public class AssetCreationRequest  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("shortDescription")
  private String shortDescription;

  @JsonProperty("location")
  private LocationCreationRequest location;

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

  @JsonProperty("isPublic")
  private Boolean isPublic;

  public AssetCreationRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the data asset.
   * @return name
  */
  @ApiModelProperty(example = "Iris Dataset", required = true, value = "The name of the data asset.")
  @NotNull

@Size(max=256) 
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AssetCreationRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * A longer description about the content of the asset. Supports rich text formatting.
   * @return description
  */
  @ApiModelProperty(example = "This is perhaps the best known database to be found in the pattern recognition literature. Fisher's paper is a classic in the field and is referenced frequently to this day. (See Duda & Hart, for example.) The data set contains 3 classes of 50 instances each, where each class refers to a type of iris plant. One class is linearly separable from the other 2; the latter are NOT linearly separable from each other.", required = true, value = "A longer description about the content of the asset. Supports rich text formatting.")
  @NotNull


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AssetCreationRequest shortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
    return this;
  }

  /**
   * Short description breifly defining the contents of the asset.
   * @return shortDescription
  */
  @ApiModelProperty(example = "This is perhaps the best known database to be found in the pattern recognition literature.", value = "Short description breifly defining the contents of the asset.")

@Size(max=512) 
  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public AssetCreationRequest location(LocationCreationRequest location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public LocationCreationRequest getLocation() {
    return location;
  }

  public void setLocation(LocationCreationRequest location) {
    this.location = location;
  }

  public AssetCreationRequest tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public AssetCreationRequest addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * A list of keywords assigned to the asset.
   * @return tags
  */
  @ApiModelProperty(value = "A list of keywords assigned to the asset.")


  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public AssetCreationRequest format(FormatEnum format) {
    this.format = format;
    return this;
  }

  /**
   * The file format of the asset. 
   * @return format
  */
  @ApiModelProperty(example = "csv", required = true, value = "The file format of the asset. ")
  @NotNull


  public FormatEnum getFormat() {
    return format;
  }

  public void setFormat(FormatEnum format) {
    this.format = format;
  }

  public AssetCreationRequest isPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * Whether the asset is accessible by anyone or not.
   * @return isPublic
  */
  @ApiModelProperty(required = true, value = "Whether the asset is accessible by anyone or not.")
  @NotNull


  public Boolean getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssetCreationRequest assetCreationRequest = (AssetCreationRequest) o;
    return Objects.equals(this.name, assetCreationRequest.name) &&
        Objects.equals(this.description, assetCreationRequest.description) &&
        Objects.equals(this.shortDescription, assetCreationRequest.shortDescription) &&
        Objects.equals(this.location, assetCreationRequest.location) &&
        Objects.equals(this.tags, assetCreationRequest.tags) &&
        Objects.equals(this.format, assetCreationRequest.format) &&
        Objects.equals(this.isPublic, assetCreationRequest.isPublic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, shortDescription, location, tags, format, isPublic);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssetCreationRequest {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    shortDescription: ").append(toIndentedString(shortDescription)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
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

