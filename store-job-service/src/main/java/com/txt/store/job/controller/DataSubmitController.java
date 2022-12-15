package com.txt.store.job.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.txt.store.job.dto.ObjectIdDataDTO;
import com.txt.store.job.dto.common.ResponseCode;
import com.txt.store.job.dto.common.RequestWithBody;
import com.txt.store.job.dto.common.ResponseWithBody;
import com.txt.store.job.dto.submit.DataSubmitDTO;
import com.txt.store.job.dto.submit.SearchSubmitDataDTO;
import com.txt.store.job.service.CommonService;
import com.txt.store.job.utils.ErrorUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;


@RestController
@Tag(name = "Data MongoDB Submit Controller", description = "Data submit Controller API")
@Slf4j
@RequiredArgsConstructor
public class DataSubmitController {

    //region INIT
    final Environment env;
    final ObjectMapper objectMapper;
    final HttpServletRequest httpServletRequest;
    final CommonService commonService;
    //endregion


    //region start api
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "500", description = "Wrong params")})
    @Operation(description = "load data submit API")
    @PostMapping(path = "/store-job/load-data-submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> loadDataSubmit(
            @RequestBody RequestWithBody<SearchSubmitDataDTO> requestWithBody,
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {

        ResponseWithBody<List<DataSubmitDTO>> response = new ResponseWithBody<>();

        try {
            String uri = httpServletRequest.getRequestURI();
            String exchangeId = StringUtils.isBlank(requestWithBody.getExchangeId())
                    ? UUID.randomUUID().toString() : requestWithBody.getExchangeId();
            MDC.put("traceId", exchangeId);
            requestWithBody.setExchangeId(exchangeId);
            response.setExchangeId(exchangeId);

            List<DataSubmitDTO> dataSubmitDTOs = null;
            dataSubmitDTOs = commonService.getListDataSubmitDTO();

            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            response.setBody(dataSubmitDTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("load data submit has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //endregion

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "500", description = "Wrong params")})
    @Operation(description = "load case detail API")
    @PostMapping(path = "/store-job/load-record-detail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> loadCaseDetail(
            @RequestBody RequestWithBody<ObjectIdDataDTO> requestWithBody,
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {

        ResponseWithBody<DataSubmitDTO> response = new ResponseWithBody<>();

        try {
            String uri = httpServletRequest.getRequestURI();
            String exchangeId = StringUtils.isBlank(requestWithBody.getExchangeId())
                    ? UUID.randomUUID().toString() : requestWithBody.getExchangeId();
            MDC.put("traceId", exchangeId);
            requestWithBody.setExchangeId(exchangeId);
            response.setExchangeId(exchangeId);

            String idObject = requestWithBody.getBody().getObjectId();
            DataSubmitDTO dataSubmitDTO = commonService.getDataSubmitDTO(idObject);
            if (dataSubmitDTO == null) {
                response.setResponseStatus(ErrorUtil.createResponseStatus(ResponseCode.KEY_E007));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            response.setBody(dataSubmitDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("load detail has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "500", description = "Wrong params")})
    @Operation(description = "update case detail API")
    @PostMapping(path = "/store-job/update-record-detail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> updateCaseDetail(
            @RequestBody RequestWithBody<DataSubmitDTO> requestWithBody,
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {

        ResponseWithBody<?> response = new ResponseWithBody<>();

        try {
            String uri = httpServletRequest.getRequestURI();
            String exchangeId = StringUtils.isBlank(requestWithBody.getExchangeId())
                    ? UUID.randomUUID().toString() : requestWithBody.getExchangeId();
            MDC.put("traceId", exchangeId);
            requestWithBody.setExchangeId(exchangeId);
            response.setExchangeId(exchangeId);

            //handle
            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            response.setBody(null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("update detail has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
