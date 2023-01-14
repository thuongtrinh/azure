package com.txt.store.job.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.blob.specialized.BlockBlobClient;
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
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureServiceImpl implements AzureService {

    @Value("${azure-storage.connectionString}")
    private String azureStorageConnectionString;

    @Value("${azure-storage.container}")
    private String azureStorageContainer;

    final BlobContainerClient blobContainerClient;
    final CloudBlobContainer cloudBlobContainer;

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
        if (StringUtils.isBlank(directoryPath)) {
            directoryPath = "";
        }

        /*CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(azureStorageConnectionString);
        CloudBlobClient cloudBlobClient = cloudStorageAccount.createCloudBlobClient();
        CloudBlobContainer cloudBlobContainer = cloudBlobClient.getContainerReference(azureStorageContainer);*/

        CloudBlobDirectory directory = cloudBlobContainer.getDirectoryReference(directoryPath);
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

//        System.out.println("------TEST genSasToken------");
//        genSasToken();

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


    public void genSasToken() {
        try {
            // Create a blob service client
//            CloudStorageAccount account = CloudStorageAccount.parse(azureStorageConnectionString);
//            CloudBlobClient blobClient = account.createCloudBlobClient();
//            CloudBlobContainer container = blobClient.getContainerReference(azureStorageContainer);

            Date expirationTime = Date.from(LocalDateTime.now().plusDays(2).atZone(ZoneOffset.UTC).toInstant());
            SharedAccessBlobPolicy sharedAccessPolicy = new SharedAccessBlobPolicy();
            sharedAccessPolicy.setPermissions(EnumSet.of(SharedAccessBlobPermissions.READ,
                    SharedAccessBlobPermissions.WRITE, SharedAccessBlobPermissions.ADD));
            sharedAccessPolicy.setSharedAccessStartTime(new Date());
            sharedAccessPolicy.setSharedAccessExpiryTime(expirationTime);

            String sasToken = cloudBlobContainer.generateSharedAccessSignature(sharedAccessPolicy, null);
            System.out.println(sasToken);
            System.out.println(cloudBlobContainer.getUri());

            //sig=HJVpU2DBSE%2B6rcRcHwFHw3zWR9YaDupRPEjv40IOUXs%3D&st=2022-12-17T15%3A11%3A32Z&se=2022-12-19T22%3A11%3A32Z&sv=2019-02-02&sp=raw&sr=c
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void genSasSignatureToken(String blobName) {
        try {
            BlobServiceClient client = new BlobServiceClientBuilder().connectionString(azureStorageConnectionString).buildClient();
            BlobClient blobClient = client.getBlobContainerClient(azureStorageContainer).getBlobClient(blobName);

            BlobSasPermission blobSasPermission = new BlobSasPermission().setReadPermission(true); // grant read permission onmy
            OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(2); // 2 days to expire

            BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime, blobSasPermission)
                    .setStartTime(OffsetDateTime.now());

            System.out.println(blobClient.generateSas(values));

            //sv=2021-10-04&st=2022-12-17T15%3A11%3A32Z&se=2022-12-19T15%3A11%3A32Z&sr=b&sp=r&sig=P39dVsp5aBc9h6PRcfjPmumGv3M4XRaFU%2BNE%2BDxR91o%3D
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
