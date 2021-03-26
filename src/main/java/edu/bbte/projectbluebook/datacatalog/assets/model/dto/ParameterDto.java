package edu.bbte.projectbluebook.datacatalog.assets.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Parameter in the form of key-value pair.
 */
@ApiModel(description = "Parameter in the form of key-value pair.")

public class ParameterDto  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("key")
  private String key;

  @JsonProperty("value")
  private String value;

  public ParameterDto key(String key) {
    this.key = key;
    return this;
  }

  /**
   * The key of the parameter.
   * @return key
  */
  @ApiModelProperty(example = "permissions", required = true, value = "The key of the parameter.")
  @NotNull

@Size(max=256) 
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public ParameterDto value(String value) {
    this.value = value;
    return this;
  }

  /**
   * The value of the parameter
   * @return value
  */
  @ApiModelProperty(example = "read$list", required = true, value = "The value of the parameter")
  @NotNull


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParameterDto parameterDto = (ParameterDto) o;
    return Objects.equals(this.key, parameterDto.key) &&
        Objects.equals(this.value, parameterDto.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParameterDto {\n");
    
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

