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
public class UserId implements Serializable {

    private static final long serialVersionUID = 3668749708532705679L;

    private final Long value;

    public static UserId from(Long value) {

        if (value == null) {
            throw new IllegalArgumentException("token value cannot be empty");
        }

        return new UserId(value);
    }

    @Converter(autoApply = true)
    public static class UserIdConverter implements AttributeConverter<UserId, Long> {

        @Override
        public Long convertToDatabaseColumn(UserId attribute) {

            if (attribute == null) {
                return null;
            }

            return attribute.getValue();
        }

        @Override
        public UserId convertToEntityAttribute(Long dbData) {

            if (StringUtils.isEmpty(dbData)) {
                return null;
            }

            return UserId.from(dbData);
        }
    }
}
