package com.txt.store.job.config;

import com.azure.core.http.ProxyOptions;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

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

}
