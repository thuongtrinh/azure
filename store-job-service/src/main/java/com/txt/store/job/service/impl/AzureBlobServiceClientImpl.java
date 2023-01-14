package com.txt.store.job.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.txt.store.job.dto.imports.BlobDTO;
import com.txt.store.job.service.AzureBlobServiceClient;
import com.txt.store.job.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AzureBlobServiceClientImpl implements AzureBlobServiceClient {

    @Value("${azure-storage.sasToken}")
    private String azureStorageSasToken;

    @Value("${azure-storage.storage-account-url}")
    private String azureStorageUrl;

    @Autowired
    private JsonUtils jsonUtils;

    @Override
    public void blobContainerClient(String containerName, String blobName) {
        BlobContainerClient blobContainerClient = createBlobServiceClient()
                .getBlobContainerClient(containerName);
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        BlobDTO blobDTO = new BlobDTO();
        blobDTO.setBlobName(blobClient.getBlobName());
        blobDTO.setBlobUrl(blobClient.getBlobUrl());
        blobDTO.setBlobProperties(jsonUtils.objectToJson(blobClient.getProperties()));
        blobDTO.setContainer(blobClient.getContainerName());
        blobDTO.setAccountName(blobClient.getAccountName());
        blobDTO.setAccountUrl(blobClient.getAccountUrl());

        System.out.println(jsonUtils.objectToJson(blobDTO));
    }

    public BlobServiceClient createBlobServiceClient() {
        return new BlobServiceClientBuilder()
                .endpoint(azureStorageUrl)
                .sasToken(azureStorageSasToken)
                .buildClient();
    }
}
