package com.kp.test.infrastructure.config.advice;

import com.kp.test.infrastructure.config.exception.KpCustomException;
import com.kp.test.infrastructure.config.exception.KpExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionAdivce extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    ResponseEntity handleException(Throwable throwable, HttpServletRequest request, WebRequest webRequest) {
        return handleException(new KpCustomException(throwable), request, webRequest);
    }

    @ExceptionHandler(KpCustomException.class)
    ResponseEntity handleException(KpCustomException exception, HttpServletRequest request, WebRequest webRequest) {

        KpExceptionCode exceptionCode = exception.getExceptionCode();

        Map<String, String> errorAttributes = new HashMap<>();
        errorAttributes.put("code", exceptionCode.name());
        errorAttributes.put("message", exceptionCode.getMessage());
        errorAttributes.put("path", request.getRequestURI());

        return ResponseEntity.status(exceptionCode.getResponseStatus())
                .body(errorAttributes);
    }


}
