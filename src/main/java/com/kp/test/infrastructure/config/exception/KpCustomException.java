package com.kp.test.infrastructure.config.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KpCustomException extends RuntimeException {

    private KpExceptionCode exceptionCode = KpExceptionCode.DEFAULT;

    @JsonValue
    public Map<String, Object> getExceptionDetail(){
        Map<String, Object> detail = new HashMap<>();
        detail.put("status", exceptionCode.getResponseStatus());
        detail.put("message", exceptionCode.getMessage());
        return detail;
    }

    public KpCustomException() {
        super();
    }

    public KpCustomException(Throwable cause) {
        super(KpExceptionCode.DEFAULT.getMessage(), cause);
    }

    public KpCustomException(KpExceptionCode kpExceptionCode) {
        super(kpExceptionCode.getMessage());
        this.exceptionCode = kpExceptionCode;
    }

    public KpCustomException(KpExceptionCode kpExceptionCode, Throwable cause) {
        super(kpExceptionCode.getMessage(), cause);
        this.exceptionCode = kpExceptionCode;
    }


}
