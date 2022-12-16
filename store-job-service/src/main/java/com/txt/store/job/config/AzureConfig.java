package com.txt.store.job.config;

import com.azure.core.http.ProxyOptions;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Configuration
public class AzureConfig {

    @Value("${azure-storage.connectionString}")
    private String azureStorageConnectionString;
    @Value("${azure-storage.container}")
    private String azureStorageContainer;

    @Bean
    public BlobServiceClient blobServiceClient() {
//        ProxyOptions options = new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("xxx.xxx.xxx.xxx", pp));
//        return new BlobServiceClientBuilder()
//                .connectionString(azureStorageConnectionString)
//                .httpClient(new NettyAsyncHttpClientBuilder().proxy(options).build())
//                .buildClient();
        return new BlobServiceClientBuilder()
                .connectionString(azureStorageConnectionString)
                .buildClient();
    }

    @Bean
    public BlobContainerClient blobContainerClient() {
        return blobServiceClient()
                .getBlobContainerClient(azureStorageContainer);
    }

    @Bean
    public CloudBlobContainer cloudBlobContainerPC() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(azureStorageConnectionString);
        CloudBlobClient cloudBlobClient = cloudStorageAccount.createCloudBlobClient();
        return cloudBlobClient.getContainerReference(azureStorageContainer);
    }

}
