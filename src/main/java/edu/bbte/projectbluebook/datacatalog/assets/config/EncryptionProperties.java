package edu.bbte.projectbluebook.datacatalog.assets.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "encryption")
public class EncryptionProperties {
    private String password;

    private String salt;
}
