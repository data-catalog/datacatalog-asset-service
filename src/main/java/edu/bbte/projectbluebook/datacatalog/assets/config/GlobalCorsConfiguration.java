package edu.bbte.projectbluebook.datacatalog.assets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;

@Configuration
public class GlobalCorsConfiguration {

    @Bean
    public WebFluxConfigurer corsConfigurer() {
        return new CorsWebFluxConfigurer();
    }

    static class CorsWebFluxConfigurer extends WebFluxConfigurerComposite {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS");
        }
    }
}