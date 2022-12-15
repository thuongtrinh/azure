package com.txt.store.job.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;

@Data
@JsonIgnoreProperties("inputStream")
public class FileDTO {

    private InputStream inputStream;
    private String contentType;
    private String name;
    private Map<String, String> metadata;
    private String blobType;
    private Date createdTime;
    private Date lastModified;
}