package com.txt.store.job.service;

import com.txt.store.job.dto.FileAzureRequestDTO;
import com.txt.store.job.dto.FileDTO;

import java.io.InputStream;
import java.util.List;

public interface AzureService {

    FileDTO getAzureBlobFile(String blobName);

    Boolean uploadFileAzureBlobFile(InputStream is, String blobName, String contentType);

    List<FileDTO> getListAzureBlobFileName(String directoryPath) throws Exception;

    boolean moveFileBetweenAzureBlob(String blobNameSource, String blobNameTarget);

    void downloadedFile(FileAzureRequestDTO fileAzureRequestDTO);
}
