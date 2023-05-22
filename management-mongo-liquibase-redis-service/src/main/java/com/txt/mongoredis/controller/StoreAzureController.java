package com.txt.mongoredis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.txt.mongoredis.dto.common.ResponseWithBody;
import com.txt.mongoredis.utils.ErrorUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@RestController
@Tag(name = "StoreAzureController", description = "StoreAzureController API")
@Slf4j
@RequiredArgsConstructor
public class StoreAzureController {

    //region INIT
    final Environment env;
    final ObjectMapper objectMapper;
    final HttpServletRequest httpServletRequest;
    final RestTemplate restTemplate;
    //endregion


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description =  "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description =  "Bad request."),
            @ApiResponse(responseCode = "500", description =  "Wrong params")})
    @Operation(description = "Upload file to azure API")
    @PostMapping(path = "/azure-job/upload-file-to-azure", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> uploadFileToAzure(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestParam(value="attachmentFile", required=false) MultipartFile attachmentFile) {

        ResponseWithBody<List<?>> response = new ResponseWithBody<>();

        try {
            String uri = httpServletRequest.getRequestURI();
            String exchangeId = UUID.randomUUID().toString();
            MDC.put("traceId", exchangeId);
            response.setExchangeId(exchangeId);

            String url="http://localhost:8085/store-job/upload-file-to-azure-store";
            String fileName = attachmentFile.getOriginalFilename();
            String contextType = attachmentFile.getContentType();
            byte [] bytes = attachmentFile.getBytes();

            HttpHeaders headerFile = new HttpHeaders();
            headerFile.setContentType(MediaType.parseMediaType(contextType));
            final ByteArrayResource byteArrayResource = new ByteArrayResource(bytes) {
                @Override
                public String getFilename() {
                    return fileName;
                }
            };

            HttpEntity<ByteArrayResource> entityFile = new HttpEntity<>(byteArrayResource, headerFile);

            MultiValueMap<String, Object> multiValuedMap = new LinkedMultiValueMap<>();
            multiValuedMap.add("attachmentFile", entityFile);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(multiValuedMap, headers);

            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            restTemplate.exchange(url, HttpMethod.POST, req, new ParameterizedTypeReference<ResponseWithBody>() {
            });

            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Upload file to azure has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
