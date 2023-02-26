package org.kenakata.Handler.ErrorHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> apiRequestException(ApiRequestException e) {

        int statusCode = HttpStatus.OK.value();
        ErrorItem errorItem = new ErrorItem(
                statusCode,
                e.getMessage()
        );

        //return response entity

        return new ResponseEntity<>(errorItem, HttpStatusCode.valueOf(statusCode));
    }



}
