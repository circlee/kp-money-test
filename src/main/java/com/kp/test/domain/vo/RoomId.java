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
public class RoomId implements Serializable {

    private static final long serialVersionUID = 3668749708532705679L;

    private final Long value;

    public static RoomId from(Long value) {

        if (value == null) {
            throw new IllegalArgumentException("RoomId value cannot be empty");
        }

        return new RoomId(value);
    }

    @Converter(autoApply = true)
    public static class RoomIdConverter implements AttributeConverter<RoomId, Long> {

        @Override
        public Long convertToDatabaseColumn(RoomId attribute) {

            if (attribute == null) {
                return null;
            }

            return attribute.getValue();
        }

        @Override
        public RoomId convertToEntityAttribute(Long dbData) {

            if (StringUtils.isEmpty(dbData)) {
                return null;
            }

            return RoomId.from(dbData);
        }
    }
}
