package edu.bbte.projectbluebook.datacatalog.assets.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AssetResponseAllOf
 */

public class AssetResponseAllOf  implements Serializable {
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

  public AssetResponseAllOf id(String id) {
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

  public AssetResponseAllOf createdAt(OffsetDateTime createdAt) {
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

  public AssetResponseAllOf updatedAt(OffsetDateTime updatedAt) {
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

  public AssetResponseAllOf ownerId(String ownerId) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssetResponseAllOf assetResponseAllOf = (AssetResponseAllOf) o;
    return Objects.equals(this.id, assetResponseAllOf.id) &&
        Objects.equals(this.createdAt, assetResponseAllOf.createdAt) &&
        Objects.equals(this.updatedAt, assetResponseAllOf.updatedAt) &&
        Objects.equals(this.ownerId, assetResponseAllOf.ownerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdAt, updatedAt, ownerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssetResponseAllOf {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
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

