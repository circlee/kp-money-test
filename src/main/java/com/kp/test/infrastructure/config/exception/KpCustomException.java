package com.kp.test.infrastructure.config.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KpCustomException extends RuntimeException {

    private HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private String message = "internal server error";

    @JsonValue
    public Map<String, Object> getExceptionDetail(){
        Map<String, Object> detail = new HashMap<>();
        detail.put("status", responseStatus.toString());
        detail.put("message", message);
        return detail;
    }

    public KpCustomException() {
        super();
    }

    public KpCustomException(HttpStatus responseStatus, String message) {
        super(message);
        this.responseStatus = responseStatus;
        this.message = message;
    }

    public KpCustomException(KpExceptionCode kpExceptionCode) {
        this(kpExceptionCode.getResponseStatus(), kpExceptionCode.getMessage());
    }

    public KpCustomException(HttpStatus responseStatus, String message, Throwable cause) {
        super(message, cause);
        this.responseStatus = responseStatus;
        this.message = message;
    }

    public KpCustomException(KpExceptionCode kpExceptionCode, Throwable cause) {
        this(kpExceptionCode.getResponseStatus(), kpExceptionCode.getMessage(), cause);
    }








}
