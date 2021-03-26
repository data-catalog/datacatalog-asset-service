package edu.bbte.projectbluebook.datacatalog.assets.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Document
public class Asset extends BaseEntity implements Serializable {
    @Indexed(unique = true)
    private String name;

    private String description;

    private String shortDescription;

    private Location location;

    private List<String> tags = new ArrayList<>();

    private String format;

    private String namespace;

    private String ownerId;

    @Field("public")
    private Boolean _public;

    private List<String> members = new ArrayList<>();
}
