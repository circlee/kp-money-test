package com.kp.test.infrastructure.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum KpExceptionCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터 입니다.")
    , INVALID_HEADER(HttpStatus.BAD_REQUEST, "잘못된 헤더 입니다.")
    , NOT_AVAILABLE_TOKEN(HttpStatus.BAD_REQUEST, "이용 불가능한 토큰입니다")
    , NOT_AVAILABLE_MONEY_SPREAD(HttpStatus.BAD_REQUEST, "이용 불가능한 뿌리기입니다")
    , NO_REMAIN_DISTRIBUTION(HttpStatus.BAD_REQUEST, "받을 수 있는 금액이 없습니다.")
    , ALREADY_RECEIVED(HttpStatus.BAD_REQUEST, "이미 받았습니다.");
    ;

    private HttpStatus responseStatus;
    private String message;

    KpExceptionCode(HttpStatus responseStatus, String message) {
        this.responseStatus = responseStatus;
        this.message = message;
    }
}
