package com.txt.store.job.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.*;
import com.txt.store.job.dto.FileAzureRequestDTO;
import com.txt.store.job.dto.FileDTO;
import com.txt.store.job.service.AzureService;
import com.txt.store.job.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureServiceImpl implements AzureService {

    @Value("${azure-storage.connectionString}")
    private String azureStorageConnectionString;

    @Value("${azure-storage.container}")
    private String azureStorageContainer;

    final BlobContainerClient blobContainerClient;
    final CloudBlobContainer cloudBlobContainerPC;

    @Autowired
    private JsonUtils jsonUtils;


    @Override
    public FileDTO getAzureBlobFile(String blobName) {
        log.info("Process getAzureBlobFile {}", blobName);
        FileDTO fileDTO = null;
        try {
            BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
            if (blobClient.exists()) {
                fileDTO = new FileDTO();
                fileDTO.setInputStream(blobClient.openInputStream());
                fileDTO.setContentType(blobClient.getProperties().getContentType());
                fileDTO.setName(blobClient.getBlobName());
                System.out.println(jsonUtils.objectToJson(fileDTO));
            } else {
                log.info("getAzureBlobFile - Not found blobName {}",blobName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getAzureBlobFile - error exception {}", e.getMessage());
        }

        return fileDTO;
    }


    @Override
    public Boolean uploadFileAzureBlobFile(InputStream isFile, String blobName, String contentType) {
        log.info("Process uploadFileAzureBlobFile - {}", blobName);

        boolean check = false;
        try {
            BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
            byte[] data = this.getBytesFromInputStream(isFile);
            InputStream inputStream = new ByteArrayInputStream(data);
            blobClient.upload(inputStream, data.length);
            blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(contentType));
            check = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("uploadFileAzureBlobFile - error exception {}", e.getMessage());
        }
        return check;
    }

    @Override
    public List<FileDTO> getListAzureBlobFileName(String directoryPath) throws Exception {
        log.info("Process getListAzureBlobFileName - {}", directoryPath);
        List<FileDTO> fileDTOs = new ArrayList<>();
        if (directoryPath == null) {
            return fileDTOs;
        }

        /*CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(azureStorageConnectionString);
        CloudBlobClient cloudBlobClient = cloudStorageAccount.createCloudBlobClient();
        CloudBlobContainer cloudBlobContainer = cloudBlobClient.getContainerReference(azureStorageContainer);*/

        CloudBlobDirectory directory = cloudBlobContainerPC.getDirectoryReference(directoryPath);
        Iterable<ListBlobItem> blobItems = directory.listBlobs();

        for (ListBlobItem fileBlob : blobItems) {
            FileDTO fileDTO = new FileDTO();
            if (fileBlob instanceof CloudBlob) {
                CloudBlob cloudBlob = (CloudBlob) fileBlob;
                String name = cloudBlob.getName();
                fileDTO.setName(name.substring(name.lastIndexOf("/") + 1, name.length()));
                fileDTO.setMetadata(cloudBlob.getMetadata());
                fileDTO.setContentType(cloudBlob.getProperties().getContentType());
                fileDTO.setBlobType(cloudBlob.getProperties().getBlobType().name());
                fileDTO.setCreatedTime(cloudBlob.getProperties().getCreatedTime());
                fileDTO.setLastModified(cloudBlob.getProperties().getLastModified());
                fileDTOs.add(fileDTO);
            }
        }

        log.info("Success getListAzureBlobFileName of path directoty {} - {}", directoryPath, jsonUtils.objectToJson(fileDTOs));
        return fileDTOs;
    }

    @Override
    public boolean moveFileBetweenAzureBlob(String blobNameSource, String blobNameTarget) {
        log.info("Process moveFileBetweenAzureBlob from {} to {}", blobNameSource, blobNameTarget);

        BlobClient blobClient = blobContainerClient.getBlobClient(blobNameSource);
        if(blobClient != null && blobClient.exists()) {
            log.info("This is the blob name is moved - {}", blobClient.getBlobName());

            BlobClient destblobclient = blobContainerClient.getBlobClient(blobNameTarget);
            destblobclient.beginCopy(blobClient.getBlobUrl(), null);
            blobClient.deleteIfExists();
            return true;
        } else {
            log.info("Not found blobNameSource - {}", blobNameSource);
        }

        return false;
    }

    @Override
    public void downloadedFile(FileAzureRequestDTO fileAzureRequestDTO) {
        // Download the blob to a local file
        // Append the string "DOWNLOAD" before the .txt extension so that you can see both files.
        String fileName = fileAzureRequestDTO.getFileName();
        String downloadFileName = fileName.replace(".xlsx", "_DOWNLOAD_" + System.currentTimeMillis() + ".xlsx");
        String localPath = ".\\";

        if(StringUtils.isNotBlank(fileAzureRequestDTO.getDirName())) {
            fileName = fileAzureRequestDTO.getDirName() + "/" + fileName;
        }

        System.out.println("\nDownloading from fileName\n\t " + fileName);
        System.out.println("\nDownloading blob to\n\t " + localPath + downloadFileName);

        BlockBlobClient blobClient = blobContainerClient.getBlobClient(fileName).getBlockBlobClient();
        blobClient.downloadToFile(localPath + downloadFileName);
    }

    public byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }
}
