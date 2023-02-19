package com.txt.store.job.service.impl;

import com.txt.store.job.constant.Constant;
import com.txt.store.job.dto.FileDTO;
import com.txt.store.job.dto.FileInfoDTO;
import com.txt.store.job.dto.FileAzureRequestDTO;
import com.txt.store.job.dto.common.ResponseCode;
import com.txt.store.job.dto.common.ResultDTO;
import com.txt.store.job.dto.imports.DataImportDTO;
import com.txt.store.job.service.AzureService;
import com.txt.store.job.service.ExcelService;
import com.txt.store.job.service.ImportFileService;
import com.txt.store.job.repository.mongo.HeaderTemplateRepository;
import com.txt.store.job.utils.ErrorUtil;
import com.txt.store.job.utils.ExcelUtil;
import com.txt.store.job.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImportFileServiceImpl implements ImportFileService {

    @Autowired
    private AzureService azureService;

    @Autowired
    private HeaderTemplateRepository headerTemplateRepository;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private JsonUtils jsonUtils;

    @Override
    public ResultDTO<List<DataImportDTO>> readFileImport(MultipartFile attachmentFile) {
        log.info("Start handle readFileImportManual excel");

        ResultDTO<List<DataImportDTO>> resultDTO = new ResultDTO<>();

        try {
            List<DataImportDTO> resultManualDTOs = null;
            if (attachmentFile != null) {

                String contentType = attachmentFile.getContentType();
                String fileName = attachmentFile.getOriginalFilename();
                InputStream inputStream = attachmentFile.getInputStream();
                log.info("Info import file manual contentType {}, fileName {}", contentType, fileName);

                if (!ExcelUtil.isValidExcelFile(fileName)) {
                    resultDTO.setStatus(ErrorUtil.createResponseStatus(ResponseCode.KEY_E001));
                    return resultDTO;
                }

                resultManualDTOs = excelService.excelToDataImportDTO(inputStream);
                resultDTO.setBody(resultManualDTOs);
            }
        } catch (Exception e) {
            log.error("readFileImportManual has been error: " + e);
            resultDTO.setStatus(ErrorUtil.createResponseStatus(ResponseCode.KEY_E005));
            return resultDTO;
        }

        return resultDTO;
    }

    @Override
    public List<DataImportDTO> readDataBlobFile(FileDTO fileDTO) {
        log.info("Process readDataBlobFile - {}", fileDTO.getName());

        List<DataImportDTO> dataImportDTOs = new ArrayList<>();

        try {
            dataImportDTOs = excelService.excelToDataImportDTO(fileDTO.getInputStream());
        } catch (Exception e) {
            log.error("readDataBlobFile has been error: " + e);
        }

        return dataImportDTOs;
    }

    @Override
    public ResultDTO<List<FileDTO>> getFileNameImportData() {
        ResultDTO<List<FileDTO>> resultDTO = new ResultDTO<>();

        try {
            String blobName = Constant.AZURE_DIR.ORIGINAL;
            List<FileDTO> fileDTOs = azureService.getListAzureBlobFileName(blobName);
            resultDTO.setBody(fileDTOs);
        } catch (Exception e) {
            log.error("getFileNameImportData has been error: " + e);
            resultDTO.setStatus(ErrorUtil.createResponseStatus(ResponseCode.KEY_E009));
        }

        return resultDTO;
    }

    @Override
    public ResultDTO<List<FileDTO>> getFileNameFollowLink(String directoryBlob) {
        ResultDTO<List<FileDTO>> resultDTO = new ResultDTO<>();

        try {
            List<FileDTO> fileDTOs = azureService.getListAzureBlobFileName(directoryBlob);
            resultDTO.setBody(fileDTOs);
        } catch (Exception e) {
            log.error("getFileNameImportData has been error: " + e);
            resultDTO.setStatus(ErrorUtil.createResponseStatus(ResponseCode.KEY_E009));
        }

        return resultDTO;
    }

    @Override
    public boolean uploadFileToAzureStore(FileInfoDTO fileInfoDTO) {
        log.info("Process uploadFileToAzureStore");

        boolean isPush = false;
        String blobName = "";

        try {
            MultipartFile multipartFile = fileInfoDTO.getAttachmentFile();
            String contentType = multipartFile.getContentType();
            String fileName = multipartFile.getOriginalFilename();
            InputStream inputStream = multipartFile.getInputStream();
            blobName = fileName;

            if (StringUtils.isNotBlank(fileInfoDTO.getBlobDirectory())) {
                blobName = fileInfoDTO.getBlobDirectory() + "/" + fileName;
            }

            isPush = azureService.uploadFileAzureBlobFile(inputStream, blobName, contentType);

            log.info("Success uploadFileToAzureStore - {}", blobName);
        } catch (Exception e) {
            log.error("uploadFileToAzureStore has been error: " + e);
        }

        return isPush;
    }

    @Override
    public List<DataImportDTO> getDataAzureStore(FileAzureRequestDTO fileAzureRequestDTO) {
        List<DataImportDTO> resultDTOS = new ArrayList<>();
        log.info("Process submitOriginFileToSys {}", fileAzureRequestDTO.getFileName());
        try {
            String blobName = Constant.AZURE_DIR.ORIGINAL + "/" + fileAzureRequestDTO.getFileName();
            FileDTO fileDTO = azureService.getAzureBlobFile(blobName);

            if (fileDTO != null) {
                resultDTOS = readDataBlobFile(fileDTO);
            }
        } catch (Exception e) {
            log.error("submitOriginFileToSys has been error: " + e);
        }
        return resultDTOS;
    }

    @Override
    public boolean moveFileBlobAzureToProcessed(String fileName) {
        log.info("Process moveFileToProcessed - {}", fileName);
        boolean isMove = false;
        try {
            String blobNameSource = Constant.AZURE_DIR.ORIGINAL + "/" + fileName;
            String blobNameTarget = Constant.AZURE_DIR.ORIGINAL + "/" + Constant.AZURE_DIR.PROCESSED + "/" + fileName;
            isMove = azureService.moveFileBetweenAzureBlob(blobNameSource, blobNameTarget);
        } catch (Exception e) {
            log.error("moveFileToProcessed has been error: " + e);
        }
        return isMove;
    }

    @Override
    public boolean moveFileBlobAzureLink(String fileNameSource, String fileNameTarget) {
        log.info("Process moveFileBlobAzureLink - {} -> {}", fileNameSource, fileNameTarget);
        boolean isMove = false;
        try {
            isMove = azureService.moveFileBetweenAzureBlob(fileNameSource, fileNameTarget);
        } catch (Exception e) {
            log.error("moveFileBlobAzureLink has been error: " + e);
        }
        return isMove;
    }

    @Override
    public boolean downloadDataAzureStore(FileAzureRequestDTO fileAzureRequestDTO) {
        azureService.downloadedFile(fileAzureRequestDTO);
        return true;
    }

    public Boolean readingOriginFileAzure(FileAzureRequestDTO fileAzureRequestDTO) {
        log.info("Process readingOriginFileAzure {}", fileAzureRequestDTO.getFileName());
        boolean isSubmit = false;

        try {
            String blobName = new StringBuilder(Constant.AZURE_DIR.ORIGINAL).append("/")
                    .append(fileAzureRequestDTO.getFileName()).toString();

            FileDTO fileDTO = azureService.getAzureBlobFile(blobName);

            if (fileDTO != null) {
                List<DataImportDTO> dataImportDTOSuccess = readDataBlobFile(fileDTO);
                System.out.println(dataImportDTOSuccess);

                createFileExcelSuccess(blobName);
                createFileExcelFail(blobName);

                // move file to PROCESSED after submission completed
                String blobNameTarget = new StringBuilder(Constant.AZURE_DIR.ORIGINAL).append("/")
                        .append(Constant.AZURE_DIR.PROCESSED).append("/")
                        .append(fileAzureRequestDTO.getFileName()).toString();

                 azureService.moveFileBetweenAzureBlob(blobName, blobNameTarget);
            }

            return true;
        } catch (Exception e) {
            log.error("readingOriginFileAzure has been error: " + e);
        }
        return isSubmit;
    }

    private void createFileExcelSuccess(String blobName) {
        log.info("createFileExcelSuccess - {}", blobName);

        try {
            FileDTO fileDTO = azureService.getAzureBlobFile(blobName);
            String originalFilename = fileDTO.getName();
            originalFilename = originalFilename.substring(originalFilename.lastIndexOf("/") + 1);
            String extFile = ExcelUtil.getFileExtension(originalFilename);
            String fileName = originalFilename.replaceAll(extFile, "");

            Path pathTemp = Files.createTempFile(fileName, "." + extFile);
            File fileTemp = pathTemp.toFile();
            FileUtils.copyInputStreamToFile(fileDTO.getInputStream(), fileTemp);
            InputStream fileInputStream = FileUtils.openInputStream(fileTemp);
            List<Integer> successList = new ArrayList<>();
            successList.add(1);
            InputStream respFile = excelService.excelDeleteRow(fileInputStream, successList);

            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            fileInfoDTO.setFileName(originalFilename);
            fileInfoDTO.setInputStream(respFile);
            fileInfoDTO.setContentType(fileDTO.getContentType());

            String blobNameSuccess = new StringBuilder(Constant.AZURE_DIR.ORIGINAL).append("/")
                    .append(Constant.AZURE_DIR.SUCCESS).toString();
            fileInfoDTO.setBlobDirectory(blobNameSuccess);

            //upload file to SUCCESS directory
            pushFileToAzureStore(fileInfoDTO);

            FileUtils.delete(fileTemp);
        } catch (Exception e) {
            log.error("createFileExcelSuccess has been error: " + e);
        }
    }

    private void createFileExcelFail(String blobName) {
        log.info("createFileExcelFail - {}", blobName);

        try {
            FileDTO fileDTO = azureService.getAzureBlobFile(blobName);
            String originalFilename = fileDTO.getName();
            originalFilename = originalFilename.substring(originalFilename.lastIndexOf("/") + 1);
            String extFile = ExcelUtil.getFileExtension(originalFilename);
            String fileName = originalFilename.replaceAll(extFile, "");

            Path pathTemp = Files.createTempFile(fileName, "." + extFile);
            File fileTemp = pathTemp.toFile();
            FileUtils.copyInputStreamToFile(fileDTO.getInputStream(), fileTemp);
            InputStream fileInputStream = FileUtils.openInputStream(fileTemp);
            List<Integer> failList = new ArrayList<>();
            failList.add(2);
            InputStream respFile = excelService.excelDeleteRow(fileInputStream, failList);

            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            fileInfoDTO.setFileName(originalFilename);
            fileInfoDTO.setInputStream(respFile);
            fileInfoDTO.setContentType(fileDTO.getContentType());

            String blobNameFail = new StringBuilder(Constant.AZURE_DIR.ORIGINAL).append("/")
                    .append(Constant.AZURE_DIR.FAIL).toString();
            fileInfoDTO.setBlobDirectory(blobNameFail);

            //upload file to FAIL directory
            pushFileToAzureStore(fileInfoDTO);

            FileUtils.delete(fileTemp);
        } catch (Exception e) {
            log.error("createFileExcelFail has been error: " + e);
        }
    }

    public boolean pushFileToAzureStore(FileInfoDTO fileInfoDTO) {
        log.info("Process pushFileToAzureStore name - {}", fileInfoDTO.getFileName());

        boolean isPush = false;
        String blobName = "";

        try {
            String contentType;
            String fileName;
            InputStream inputStream;
            MultipartFile multipartFile = fileInfoDTO.getAttachmentFile();
            if (ObjectUtils.isNotEmpty(multipartFile)) {
                inputStream = multipartFile.getInputStream();
                contentType = multipartFile.getContentType();
                fileName = multipartFile.getOriginalFilename();
            } else {
                inputStream = fileInfoDTO.getInputStream();
                contentType = fileInfoDTO.getContentType();
                fileName = fileInfoDTO.getFileName();
            }

            blobName = fileName;
            if (StringUtils.isNotBlank(fileInfoDTO.getBlobDirectory())) {
                blobName = fileInfoDTO.getBlobDirectory() + "/" + fileName;
            }

            isPush = azureService.uploadFileAzureBlobFile(inputStream, blobName, contentType);

            log.info("Process pushFileToAzureStore is {}", isPush);
        } catch (Exception e) {
            log.error("pushFileToAzureStore has been error: " + e);
        }

        return isPush;
    }

}
