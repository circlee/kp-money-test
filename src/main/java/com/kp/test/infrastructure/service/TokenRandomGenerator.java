package com.kp.test.infrastructure.service;

import com.kp.test.domain.service.TokenGenerator;
import com.kp.test.domain.vo.Token;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class TokenRandomGenerator implements TokenGenerator {

    private static int TOKEN_KEY_LENGTH = 3;

    @Override
    public Token generate() {
        String tokenValue = RandomStringUtils.randomAlphanumeric(TOKEN_KEY_LENGTH);
        return Token.from(tokenValue);
    }

}
