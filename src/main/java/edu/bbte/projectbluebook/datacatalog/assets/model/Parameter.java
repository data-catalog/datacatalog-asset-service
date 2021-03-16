package edu.bbte.projectbluebook.datacatalog.assets.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Parameter implements Serializable {
    private String key;

    private String value;
}
