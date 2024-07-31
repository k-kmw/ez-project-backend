package com.youngcha.ez.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Auth
    ACCESS_TOKEN_NOT_FOUND("Access token을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_EXPIRED("Access token이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("유효하지 않은 Access Token입니다.", HttpStatus.UNAUTHORIZED),

    REFRESH_TOKEN_NOT_FOUND("Refresh token을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_EXPIRED("Refresh token이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("유효하지 않은 Refresh Token입니다.", HttpStatus.UNAUTHORIZED),

    METHOD_NOT_ALLOWED("허용되지 않은 Method 입니다.", HttpStatus.METHOD_NOT_ALLOWED);

    private final String message;
    private final HttpStatus httpStatus;
}
