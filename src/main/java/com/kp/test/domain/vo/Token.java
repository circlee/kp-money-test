package com.kp.test.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Token implements Serializable {

    private static final long serialVersionUID = 8839088380398635828L;

    public static int TOKEN_VALUE_LENGHT = 3;

    private final String value;

    public static Token from(String tokenValue) {

        if (StringUtils.isEmpty(tokenValue)) {
            throw new IllegalArgumentException("token value cannot be empty");
        }

        if (tokenValue.length() != TOKEN_VALUE_LENGHT) {
            throw new IllegalArgumentException("token value length must be " + TOKEN_VALUE_LENGHT);
        }

        return new Token(tokenValue);
    }

    @Converter(autoApply = true)
    public static class TokenConverter implements AttributeConverter<Token, String> {

        @Override
        public String convertToDatabaseColumn(Token attribute) {

            if (attribute == null) {
                return null;
            }

            return attribute.getValue();
        }

        @Override
        public Token convertToEntityAttribute(String dbData) {

            if (StringUtils.isEmpty(dbData)) {
                return null;
            }

            return Token.from(dbData);
        }
    }
}
