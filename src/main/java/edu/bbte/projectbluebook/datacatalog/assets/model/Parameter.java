package edu.bbte.projectbluebook.datacatalog.assets.model;

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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-11-12T18:33:53.411300+02:00[Europe/Bucharest]")

public class Parameter  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("key")
  private String key;

  @JsonProperty("value")
  private String value;

  public Parameter key(String key) {
    this.key = key;
    return this;
  }

  /**
   * The key of the parameter.
   * @return key
  */
  @ApiModelProperty(value = "The key of the parameter.")


  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Parameter value(String value) {
    this.value = value;
    return this;
  }

  /**
   * The value of the parameter
   * @return value
  */
  @ApiModelProperty(value = "The value of the parameter")


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
    Parameter parameter = (Parameter) o;
    return Objects.equals(this.key, parameter.key) &&
        Objects.equals(this.value, parameter.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Parameter {\n");
    
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

