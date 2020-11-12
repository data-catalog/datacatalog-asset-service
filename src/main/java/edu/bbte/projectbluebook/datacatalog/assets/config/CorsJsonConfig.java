package edu.bbte.projectbluebook.datacatalog.assets.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CorsJsonConfig {

    @JsonProperty
    private List<String> headers = new ArrayList<>();

    @JsonProperty
    private List<String> methods = new ArrayList<>();

    @JsonProperty
    private List<String> origins = new ArrayList<>();


    public List<String> getOrigins() {
        return origins;
    }

    public void setOrigins(List<String> origins) {
        this.origins = origins;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }
}

