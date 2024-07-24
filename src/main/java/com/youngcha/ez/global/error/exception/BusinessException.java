package com.youngcha.ez.global.error.exception;

import com.youngcha.ez.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String invalidValue;
    private final String fieldName;
    private final ErrorCode errorCode;

    public BusinessException(String invalidValue, String fieldName, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.invalidValue = invalidValue;
        this.fieldName = fieldName;
        this.errorCode = errorCode;
    }
}
