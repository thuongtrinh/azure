package com.txt.mongoredis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class DataExceptionHandler {

    @ExceptionHandler({InvalidAttributeException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public DataErrorMessage handleInvalidAttributeValueException(Exception ex, WebRequest request) {
        return new DataErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public DataErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
        DataErrorMessage message = new DataErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }
}
