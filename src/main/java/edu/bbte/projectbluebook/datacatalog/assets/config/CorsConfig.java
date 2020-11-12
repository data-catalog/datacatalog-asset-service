package edu.bbte.projectbluebook.datacatalog.assets.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        ObjectMapper objectMapper = new ObjectMapper();
        CorsJsonConfig corsJsonConfig = new CorsJsonConfig();

        try (InputStream inputStream = CorsConfig.class.getResourceAsStream("/application.json")) {
            corsJsonConfig = objectMapper.readValue(inputStream, CorsJsonConfig.class);
        } catch (IOException e) {
            System.out.println("NOT GOOD");
        }
        final List<String> headers = corsJsonConfig.getHeaders();
        final List<String> methods = corsJsonConfig.getMethods();
        final List<String> origins = corsJsonConfig.getOrigins();
        System.out.println(String.valueOf(methods));
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders(headers.toArray(new String[headers.size()]))
                        .allowedMethods(methods.toArray(new String[methods.size()]))
                        .allowedOrigins(origins.toArray(new String[origins.size()]));
            }
        };
    }
}

