package edu.bbte.projectbluebook.datacatalog.assets.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * TokenInfoRequest
 */

public class TokenInfoRequest  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("token")
  private String token;

  /**
   * A hint about the type of the token submitted for introspection.
   */
  public enum TokenTypeHintEnum {
    ACCESS_TOKEN("access_token"),
    
    API_KEY("api_key");

    private String value;

    TokenTypeHintEnum(String value) {
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
    public static TokenTypeHintEnum fromValue(String value) {
      for (TokenTypeHintEnum b : TokenTypeHintEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("token_type_hint")
  private TokenTypeHintEnum tokenTypeHint;

  public TokenInfoRequest token(String token) {
    this.token = token;
    return this;
  }

  /**
   * The string value of the token. 
   * @return token
  */
  @ApiModelProperty(required = true, value = "The string value of the token. ")
  @NotNull


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public TokenInfoRequest tokenTypeHint(TokenTypeHintEnum tokenTypeHint) {
    this.tokenTypeHint = tokenTypeHint;
    return this;
  }

  /**
   * A hint about the type of the token submitted for introspection.
   * @return tokenTypeHint
  */
  @ApiModelProperty(value = "A hint about the type of the token submitted for introspection.")


  public TokenTypeHintEnum getTokenTypeHint() {
    return tokenTypeHint;
  }

  public void setTokenTypeHint(TokenTypeHintEnum tokenTypeHint) {
    this.tokenTypeHint = tokenTypeHint;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TokenInfoRequest tokenInfoRequest = (TokenInfoRequest) o;
    return Objects.equals(this.token, tokenInfoRequest.token) &&
        Objects.equals(this.tokenTypeHint, tokenInfoRequest.tokenTypeHint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, tokenTypeHint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TokenInfoRequest {\n");
    
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    tokenTypeHint: ").append(toIndentedString(tokenTypeHint)).append("\n");
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