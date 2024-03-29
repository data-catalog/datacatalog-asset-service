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
 * LocationCreationRequest
 */

public class LocationCreationRequest  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("type")
  private String type;

  @JsonProperty("parameters")
  @Valid
  private List<ParameterDto> parameters = new ArrayList<>();

  public LocationCreationRequest type(String type) {
    this.type = type;
    return this;
  }

  /**
   * The location type.
   * @return type
  */
  @ApiModelProperty(example = "url", required = true, value = "The location type.")
  @NotNull


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public LocationCreationRequest parameters(List<ParameterDto> parameters) {
    this.parameters = parameters;
    return this;
  }

  public LocationCreationRequest addParametersItem(ParameterDto parametersItem) {
    this.parameters.add(parametersItem);
    return this;
  }

  /**
   * Get parameters
   * @return parameters
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

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
    LocationCreationRequest locationCreationRequest = (LocationCreationRequest) o;
    return Objects.equals(this.type, locationCreationRequest.type) &&
        Objects.equals(this.parameters, locationCreationRequest.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, parameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationCreationRequest {\n");
    
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

