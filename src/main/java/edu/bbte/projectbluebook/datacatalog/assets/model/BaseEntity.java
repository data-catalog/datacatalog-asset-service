package edu.bbte.projectbluebook.datacatalog.assets.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Document
public abstract class BaseEntity implements Serializable {
    @Id
    protected String id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    protected OffsetDateTime createdAt;


    // FIXME: updatedAt field is not saved to the DB
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    protected OffsetDateTime updatedAt;
}

