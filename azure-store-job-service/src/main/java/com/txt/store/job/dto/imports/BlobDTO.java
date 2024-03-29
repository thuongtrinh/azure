package com.txt.store.job.dto.imports;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class BlobDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String blobName;
    private String blobUrl;
    private String container ;
    private String blobProperties;
    private String accountUrl;
    private String accountName;
}