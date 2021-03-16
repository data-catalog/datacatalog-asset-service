package edu.bbte.projectbluebook.datacatalog.assets.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import edu.bbte.projectbluebook.datacatalog.assets.model.dto.ParameterDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * The location where the asset data can be found.
 */
@ApiModel(description = "The location where the asset data can be found.")

public class LocationResponse  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("type")
  private String type;

  @JsonProperty("parameters")
  @Valid
  private List<ParameterDto> parameters = null;

  public LocationResponse type(String type) {
    this.type = type;
    return this;
  }

  /**
   * The location type.
   * @return type
  */
  @ApiModelProperty(example = "url", value = "The location type.")


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public LocationResponse parameters(List<ParameterDto> parameters) {
    this.parameters = parameters;
    return this;
  }

  public LocationResponse addParametersItem(ParameterDto parametersItem) {
    if (this.parameters == null) {
      this.parameters = new ArrayList<>();
    }
    this.parameters.add(parametersItem);
    return this;
  }

  /**
   * Get parameters
   * @return parameters
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<ParameterDto> getParameters() {
    return parameters;
  }

  public void setParameters(List<ParameterDto> parameters) {
    this.parameters = parameters;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationResponse locationResponse = (LocationResponse) o;
    return Objects.equals(this.type, locationResponse.type) &&
        Objects.equals(this.parameters, locationResponse.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, parameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationResponse {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
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

