package com.txt.store.job.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.txt.store.job.constant.Constant;
import com.txt.store.job.dto.FileDTO;
import com.txt.store.job.dto.FileInfoDTO;
import com.txt.store.job.dto.FileAzureRequestDTO;
import com.txt.store.job.dto.common.RequestWithBody;
import com.txt.store.job.dto.common.ResponseCode;
import com.txt.store.job.dto.common.ResponseWithBody;
import com.txt.store.job.dto.common.ResultDTO;
import com.txt.store.job.dto.imports.DataImportDTO;
import com.txt.store.job.service.CommonService;
import com.txt.store.job.service.ImportFileService;
import com.txt.store.job.utils.ErrorUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;


@RestController
@Tag(name = "Import Data Controller", description = "Import Data Controller API")
@Slf4j
@RequiredArgsConstructor
public class ImportDataController {

    //region INIT
    final Environment env;
    final ObjectMapper objectMapper;
    final HttpServletRequest httpServletRequest;
    final ImportFileService importFileService;
    final CommonService commonService;
    //endregion


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description =  "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description =  "Bad request."),
            @ApiResponse(responseCode = "500", description =  "Wrong params")})
    @Operation(description = "upload file to store API")
    @PostMapping(path = "/store-job/upload-file-to-azure-store", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> uploadFileToAzureStore(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestParam(value="attachmentFile", required=false) MultipartFile attachmentFile) {

        ResponseWithBody<List<DataImportDTO>> response = new ResponseWithBody<>();

        try {
            String uri = httpServletRequest.getRequestURI();
            String exchangeId = UUID.randomUUID().toString();
            MDC.put("traceId", exchangeId);
            response.setExchangeId(exchangeId);

            ResultDTO<List<DataImportDTO>> resultlDTOs = importFileService.readFileImport(attachmentFile);

            if(ObjectUtils.isNotEmpty(resultlDTOs.getStatus())) {
                response.setResponseStatus(resultlDTOs.getStatus());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else if(ObjectUtils.isNotEmpty(resultlDTOs.getBody()) && resultlDTOs.getBody().size() > Constant.LIMIT) {
                response.setResponseStatus(ErrorUtil.createResponseStatus(ResponseCode.KEY_E002));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            fileInfoDTO.setAttachmentFile(attachmentFile);
            fileInfoDTO.setBlobDirectory(Constant.AZURE_DIR.ORIGINAL);
            Boolean checkUpload = importFileService.uploadFileToAzureStore(fileInfoDTO);

            if(!checkUpload) {
                response.setResponseStatus(ErrorUtil.createResponseStatus(ResponseCode.KEY_E008));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            response.setBody(resultlDTOs.getBody());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("import file to azure store has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description =  "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description =  "Bad request."),
            @ApiResponse(responseCode = "500", description =  "Wrong params")})
    @Operation(description = "load original file in store API")
    @GetMapping(path = "/store-job/load-info-file-in-azure-store", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> loadFileInAzureStore(
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {

        ResponseWithBody<List<FileDTO>> response = new ResponseWithBody<>();

        try {
            String uri = httpServletRequest.getRequestURI();
            String exchangeId = UUID.randomUUID().toString();
            MDC.put("traceId", exchangeId);
            response.setExchangeId(exchangeId);

            //handle
            ResultDTO<List<FileDTO>> resultlDTOs = importFileService.getFileNameImportData();
            if(ObjectUtils.isNotEmpty(resultlDTOs.getStatus())) {
                response.setResponseStatus(resultlDTOs.getStatus());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            response.setBody(resultlDTOs.getBody());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("load info file in azure store has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description =  "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description =  "Bad request."),
            @ApiResponse(responseCode = "500", description =  "Wrong params")})
    @Operation(description = "load original file in store API")
    @GetMapping(path = "/store-job/move-file-in-azure-store", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> moveFileInAzureStore(
            @RequestParam(value = "fileName" ) String fileName,
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {

        ResponseWithBody<List<FileDTO>> response = new ResponseWithBody<>();

        try {
            String uri = httpServletRequest.getRequestURI();
            String exchangeId = UUID.randomUUID().toString();
            MDC.put("traceId", exchangeId);
            response.setExchangeId(exchangeId);

            boolean move = importFileService.moveFileBlobAzureToProcessed(fileName);
            if(!move) {
                log.error("moveFileInAzureStore has error encountered");
                response.setResponseStatus(ErrorUtil.createErrorResponseStatus("moveFileInAzureStore failed"));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            response.setBody(null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("move file in azure store has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description =  "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description =  "Bad request."),
            @ApiResponse(responseCode = "500", description =  "Wrong params")})
    @Operation(description = "load original file follow link API")
    @GetMapping(path = "/store-job/move-file-follow-link", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> moveFileFollowLink(
            @RequestParam(value = "fileNameSource" ) String fileNameSource,
            @RequestParam(value = "fileNameTarget" ) String fileNameTarget,
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {

        ResponseWithBody<List<FileDTO>> response = new ResponseWithBody<>();

        try {
            String uri = httpServletRequest.getRequestURI();
            String exchangeId = UUID.randomUUID().toString();
            MDC.put("traceId", exchangeId);
            response.setExchangeId(exchangeId);

            boolean move = importFileService.moveFileBlobAzureLink(fileNameSource, fileNameTarget);
            if(!move) {
                log.error("moveFileFollowLink has error encountered");
                response.setResponseStatus(ErrorUtil.createErrorResponseStatus("moveFileFollowLink failed"));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            response.setBody(null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("move file in azure store has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description =  "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description =  "Bad request."),
            @ApiResponse(responseCode = "500", description =  "Wrong params")})
    @Operation(description = "get data from azure store API")
    @PostMapping(path = "/store-job/get-data-from-azure-store", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> getDataAzureStore(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestBody RequestWithBody<FileAzureRequestDTO> requestWithBody) {

        ResponseWithBody<List<DataImportDTO>> response = new ResponseWithBody<>();

        try {
            String uri = httpServletRequest.getRequestURI();
            String exchangeId = StringUtils.isBlank(requestWithBody.getExchangeId())
                    ? UUID.randomUUID().toString() : requestWithBody.getExchangeId();
            MDC.put("traceId", exchangeId);
            requestWithBody.setExchangeId(exchangeId);
            response.setExchangeId(exchangeId);

            //handle
            FileAzureRequestDTO fileAzureRequestDTO = requestWithBody.getBody();
            List<DataImportDTO> dtos = importFileService.getDataAzureStore(fileAzureRequestDTO);
            if(ObjectUtils.isEmpty(dtos)) {
                response.setResponseStatus(ErrorUtil.createResponseStatus(ResponseCode.KEY_E010));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("get data from azure store successfully {}", fileAzureRequestDTO.getFileName());
            commonService.saveDataImport(dtos);

            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            response.setBody(dtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("get data from azure store has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description =  "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description =  "Bad request."),
            @ApiResponse(responseCode = "500", description =  "Wrong params")})
    @Operation(description = "reading original file azure API")
    @PostMapping(path = "/store-job/reading-file-azure-and-process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> readingFileAzure(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestBody RequestWithBody<FileAzureRequestDTO> requestWithBody) {
//            @RequestParam(value="attachmentFile", required=false) MultipartFile attachmentFile) {

        ResponseWithBody<Boolean> response = new ResponseWithBody<>();

        try {
            String uri = httpServletRequest.getRequestURI();
            log.info("Process readingOriginFileAzure {}", uri);

//            String exchangeId = StringUtils.isBlank(requestWithBody.getExchangeId())
//                    ? UUID.randomUUID().toString() : requestWithBody.getExchangeId();
//            MDC.put("traceId", exchangeId);
//            requestWithBody.setExchangeId(exchangeId);
//            response.setExchangeId(exchangeId);
//
            FileAzureRequestDTO fileAzureRequestDTO = requestWithBody.getBody();

            Boolean isSubmit = importFileService.readingOriginFileAzure(fileAzureRequestDTO);
            if(!isSubmit) {
                response.setResponseStatus(ErrorUtil.createResponseStatus(ResponseCode.E001));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("readingOriginFileAzure successfully {}", fileAzureRequestDTO.getFileName());

            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            response.setBody(isSubmit);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("readingOriginFileAzure has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
