package edu.bbte.projectbluebook.datacatalog.assets.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Location implements Serializable {
    private String type;

    private List<Parameter> parameters = new ArrayList<>();
}
