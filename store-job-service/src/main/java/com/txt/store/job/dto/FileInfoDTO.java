package com.txt.store.job.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Data
public class FileInfoDTO {

    private String blobDirectory;
    private String fileName;
    private String contentType;
    private MultipartFile attachmentFile;
    private InputStream inputStream;

}