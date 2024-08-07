package com.youngcha.ez.global.error;

import com.youngcha.ez.global.error.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BasicExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException e) {

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(e.getMessage() + " " + e.getFieldName() + " : " + e.getInvalidValue());
    }
}
