package com.youngcha.ez.global.error;

import com.youngcha.ez.global.error.exception.Exception400;
import com.youngcha.ez.global.error.exception.Exception401;
import com.youngcha.ez.global.error.exception.Exception403;
import com.youngcha.ez.global.error.exception.Exception500;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BasicExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<String> exception400(Exception400 e) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<String> exception401(Exception401 e) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<String> exception403(Exception403 e) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<String> exception500(Exception500 e) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
