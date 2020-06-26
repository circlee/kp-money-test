package com.kp.test.infrastructure.config.advice;

import com.kp.test.infrastructure.config.exception.KpCustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionAdivce extends ResponseEntityExceptionHandler {

    @ExceptionHandler(KpCustomException.class)
    ResponseEntity handleException(KpCustomException exception, WebRequest request) {
        HttpStatus responseStatus = exception.getResponseStatus();
        return handleExceptionInternal(exception, exception, null, responseStatus, request);
    }
}
