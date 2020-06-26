package com.kp.test.infrastructure.config.advice;

import com.kp.test.infrastructure.config.exception.KpCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionAdivce extends ResponseEntityExceptionHandler {

    private DefaultErrorAttributes errorAttributes = new DefaultErrorAttributes();

    @ExceptionHandler(Throwable.class)
    ResponseEntity handleException(Throwable throwable, WebRequest request) {
        return handleException(new KpCustomException(throwable), request);
    }

    @ExceptionHandler(KpCustomException.class)
    ResponseEntity handleException(KpCustomException exception, WebRequest request) {
        HttpStatus responseStatus = exception.getResponseStatus();

        Map<String, Object> currentErrorAttributes =  errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults());

        return handleExceptionInternal(exception, currentErrorAttributes, null, responseStatus, request);
    }
}
