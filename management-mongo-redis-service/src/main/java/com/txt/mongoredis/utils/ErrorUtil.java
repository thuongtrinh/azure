package com.txt.mongoredis.utils;

import com.txt.mongoredis.dto.common.Error;
import com.txt.mongoredis.constant.Constant;
import com.txt.mongoredis.dto.common.*;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class ErrorUtil {

    public static ObjError createObjectError(String code, String description) {
        ObjError objError = new ObjError();
        objError.setErrorCode(code);
        objError.setErrorDescription(description);
        objError.setErrorTime(DateUtil.dateToString(new Date(), DateUtil.YYYYMMDDHHMMSS));
        return objError;
    }

    public static ObjError createObjectErrorDes(ResponseCode errorCode) {
        ObjError objError = new ObjError();
        objError.setErrorCode(errorCode.getCode());
        objError.setErrorDescription(errorCode.getDescription());
        objError.setErrorTime(DateUtil.dateToString(new Date(), DateUtil.YYYYMMDDHHMMSS));
        return objError;
    }

    public static ObjErrorList createObjectErrorListDes(ResponseCode errorCode, String fieldName) {
        ObjErrorList objErrorList = new ObjErrorList();
        objErrorList.setErrorCode(errorCode.getCode());
        objErrorList.setErrorDescription(errorCode.getDescription());
        objErrorList.setErrorField(fieldName);
        return objErrorList;
    }

    public static ObjErrorList createObjectErrorListDes(ResponseCode errorCode, String fieldName, String prefix) {
        ObjErrorList objErrorList = createObjectErrorListDes(errorCode, fieldName);
        objErrorList.setErrorFieldPrefix(prefix);
        return objErrorList;
    }

    public static ObjErrorList createObjectErrorListDes(String errorCode, String description, String fieldName) {
        ObjErrorList objErrorList = new ObjErrorList();
        objErrorList.setErrorCode(errorCode);
        objErrorList.setErrorDescription(description);
        objErrorList.setErrorField(fieldName);
        return objErrorList;
    }

    public static ObjResult creatErrorResult(ResponseCode errorCode, String exchangeId) {
        ObjResult objResult = new ObjResult();
        objResult.setExchangeId(exchangeId);
        objResult.setStatus(ResponseCode.FAILED.getDescription());
        objResult.setOErrorResult(createObjectErrorDes(errorCode));
        return objResult;
    }

    public static ResponseStatus createResponseStatus(HttpStatus httpStatus) {
        ResponseStatus status = new ResponseStatus();
        status.setCode(httpStatus.value());
        status.setMessage(httpStatus.getReasonPhrase());
        return status;
    }

    public static ResponseStatus createResponseStatus(HttpStatus httpStatus, List<com.txt.mongoredis.dto.common.Error> errors) {
        ResponseStatus status = new ResponseStatus();
        status.setCode(httpStatus.value());
        status.setMessage(httpStatus.getReasonPhrase());
        status.setErrors(errors);
        return status;
    }

    public static ResponseStatus createErrorResponseStatus(String errorMsg) {
        ResponseStatus status = new ResponseStatus();
        status.setCode(Constant.API_CODE_FAILED);
        status.setMessage(errorMsg);
        return status;
    }

    public static ResponseStatus createResponseStatus(ResponseCode responseCode) {
        com.txt.mongoredis.dto.common.Error error = new com.txt.mongoredis.dto.common.Error();
        error.setMessage(responseCode.getDescription());
        error.setCode(responseCode.getCode());

        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setErrors(Collections.singletonList(error));
        responseStatus.setCode(Constant.API_CODE_FAILED);
        responseStatus.setMessage(responseCode.getDescription());
        return responseStatus;
    }

    public static ResponseStatus createResponseStatusFromErrorList(List<ObjErrorList> objErrorList) {
        ResponseStatus responseStatus = new ResponseStatus();
        if (CollectionUtils.isEmpty(objErrorList)) {
            responseStatus.setCode(Constant.API_CODE_SUCCESS);
            responseStatus.setMessage(Constant.API_MSG_SUCCESS);
            return responseStatus;
        }
        List<com.txt.mongoredis.dto.common.Error> errors = new ArrayList<>();
        for (ObjErrorList o : objErrorList) {
            com.txt.mongoredis.dto.common.Error error = new Error();
            error.setCode(o.getErrorCode());
            error.setMessage(o.getErrorDescription());
            error.setType(o.getErrorField());
            errors.add(error);
        }
        responseStatus.setCode(Constant.API_CODE_FAILED);
        responseStatus.setMessage(Constant.API_MSG_FAILED);
        responseStatus.setErrors(errors);
        return responseStatus;
    }

}