package com.txt.mongoredis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.txt.mongoredis.dto.UserInfoDTO;
import com.txt.mongoredis.dto.common.ResponseWithBody;
import com.txt.mongoredis.entities.mongo.UserInfo;
import com.txt.mongoredis.services.UserServiceService;
import com.txt.mongoredis.utils.ErrorUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;


@RestController
@Tag(name = "MongoRedisController", description = "MongoRedisController API")
@Slf4j
@RequiredArgsConstructor
public class MongoRedisController {

    //region INIT
    final Environment env;
    final ObjectMapper objectMapper;
    final HttpServletRequest httpServletRequest;
    final RestTemplate restTemplate;
    final UserServiceService userServiceService;
    //endregion


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Indicates the requested data were returned."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "500", description = "Wrong params")})
    @Operation(description = "get list users API")
    @PostMapping(path = "/db-job/get-list-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody> getListUsers(
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {

        ResponseWithBody<List<?>> response = new ResponseWithBody<>();
        String uri = httpServletRequest.getRequestURI();
        String exchangeId = UUID.randomUUID().toString();
        MDC.put("traceId", exchangeId);
        response.setExchangeId(exchangeId);

        try {
            List<UserInfoDTO> userInfos = userServiceService.getListUser();
            response.setResponseStatus(ErrorUtil.createResponseStatusFromErrorList(null));
            response.setBody(userInfos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("getListUsers has error encountered {}", e);
            response.setResponseStatus(ErrorUtil.createErrorResponseStatus(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
