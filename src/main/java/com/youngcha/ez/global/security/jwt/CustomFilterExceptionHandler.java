package com.youngcha.ez.global.security.jwt;

import com.youngcha.ez.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomFilterExceptionHandler {

    static public void handleException(HttpServletResponse response, ErrorCode errorCode)
        throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print("{\"error\": \"" + errorCode.getMessage() + "\"}");
        writer.flush();
    }
}
