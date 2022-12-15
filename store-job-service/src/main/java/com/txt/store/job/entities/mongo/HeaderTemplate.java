package com.txt.store.job.entities.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Document(collection = "header_template")
public class HeaderTemplate {

    private ObjectId _id;
    private String code;
    private String name;
    private List<String> headerNames;
}
