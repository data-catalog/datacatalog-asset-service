package edu.bbte.projectbluebook.datacatalog.assets.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TokenInfoResponse
 */

public class TokenInfoResponse  implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("username")
    private String username;

    /**
     * the role for the user that the token was issued to.
     */
    public enum RoleEnum {
        USER("user"),

        ADMIN("admin");

        private String value;

        RoleEnum(String value) {
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
        public static RoleEnum fromValue(String value) {
            for (RoleEnum b : RoleEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    @JsonProperty("role")
    private RoleEnum role;

    @JsonProperty("exp")
    private Integer exp;

    @JsonProperty("iat")
    private Integer iat;

    public TokenInfoResponse active(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * This is a boolean value of whether or not the presented token is currently active.
     * @return active
     */
    @ApiModelProperty(required = true, value = "This is a boolean value of whether or not the presented token is currently active.")
    @NotNull


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public TokenInfoResponse userId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * The unique identifier for the user that the token was issued to.
     * @return userId
     */
    @ApiModelProperty(value = "The unique identifier for the user that the token was issued to.")

    @Size(min=1)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TokenInfoResponse username(String username) {
        this.username = username;
        return this;
    }

    /**
     * The username of the user that the token was issued to.
     * @return username
     */
    @ApiModelProperty(example = "user1", value = "The username of the user that the token was issued to.")

    @Size(min=1)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TokenInfoResponse role(RoleEnum role) {
        this.role = role;
        return this;
    }

    /**
     * the role for the user that the token was issued to.
     * @return role
     */
    @ApiModelProperty(value = "the role for the user that the token was issued to. ")


    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public TokenInfoResponse exp(Integer exp) {
        this.exp = exp;
        return this;
    }

    /**
     * The unix timestamp indicating when this token will expire.
     * @return exp
     */
    @ApiModelProperty(example = "1437275311", value = "The unix timestamp indicating when this token will expire.")


    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public TokenInfoResponse iat(Integer iat) {
        this.iat = iat;
        return this;
    }

    /**
     * Unix timestamp indicating when this token was originally issued.
     * @return iat
     */
    @ApiModelProperty(example = "1419350238", value = "Unix timestamp indicating when this token was originally issued.")


    public Integer getIat() {
        return iat;
    }

    public void setIat(Integer iat) {
        this.iat = iat;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TokenInfoResponse tokenInfoResponse = (TokenInfoResponse) o;
        return Objects.equals(this.active, tokenInfoResponse.active) &&
            Objects.equals(this.userId, tokenInfoResponse.userId) &&
            Objects.equals(this.username, tokenInfoResponse.username) &&
            Objects.equals(this.role, tokenInfoResponse.role) &&
            Objects.equals(this.exp, tokenInfoResponse.exp) &&
            Objects.equals(this.iat, tokenInfoResponse.iat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, userId, username, role, exp, iat);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TokenInfoResponse {\n");

        sb.append("    active: ").append(toIndentedString(active)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    role: ").append(toIndentedString(role)).append("\n");
        sb.append("    exp: ").append(toIndentedString(exp)).append("\n");
        sb.append("    iat: ").append(toIndentedString(iat)).append("\n");
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

