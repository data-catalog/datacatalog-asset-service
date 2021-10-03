package edu.bbte.projectbluebook.datacatalog.assets;

import edu.bbte.projectbluebook.datacatalog.assets.config.ClientProperties;
import edu.bbte.projectbluebook.datacatalog.assets.config.EncryptionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = { ReactiveSecurityAutoConfiguration.class })
@EnableConfigurationProperties({ClientProperties.class, EncryptionProperties.class})
public class AssetsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssetsApplication.class, args);
    }
}
