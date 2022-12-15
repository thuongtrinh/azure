package com.txt.store.job.service;

import com.txt.store.job.dto.FileDTO;
import com.txt.store.job.dto.FileInfoDTO;
import com.txt.store.job.dto.FileAzureRequestDTO;
import com.txt.store.job.dto.common.ResultDTO;
import com.txt.store.job.dto.imports.DataImportDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImportFileService {

    ResultDTO<List<DataImportDTO>> readFileImport(MultipartFile attachmentFile);

    List<DataImportDTO> readDataBlobFile(FileDTO fileDTO);

    ResultDTO<List<FileDTO>> getFileNameImportData();

    List<DataImportDTO> getDataAzureStore(FileAzureRequestDTO fileLoadDTO);

    boolean uploadFileToAzureStore(FileInfoDTO fileInfoDTO);

    boolean moveFileBlobAzureToProcessed(String fileName);

    Boolean readingOriginFileAzure(FileAzureRequestDTO fileAzureRequestDTO);

    boolean moveFileBlobAzureLink(String fileNameSource, String fileNameTarget);
}
