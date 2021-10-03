package edu.bbte.projectbluebook.datacatalog.assets.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Data
public class Location implements Serializable {
    private String type;

    private List<Parameter> parameters = new ArrayList<>();
}
