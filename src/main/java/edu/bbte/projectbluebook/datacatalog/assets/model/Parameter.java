package edu.bbte.projectbluebook.datacatalog.assets.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class Parameter implements Serializable {
    private String key;

    private String value;
}
