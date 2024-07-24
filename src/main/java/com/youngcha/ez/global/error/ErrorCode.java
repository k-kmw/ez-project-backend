package com.youngcha.ez.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Auth
    TOKEN_NOT_FOUND("Access token is missing.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("Access token expired.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("Invalid access token.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus httpStatus;
}
