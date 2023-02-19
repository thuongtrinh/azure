package com.txt.store.job.service;

public interface AzureBlobServiceClient {

    void blobContainerClient(String containerName, String blobName);
}
