package com.kp.test.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MoneySpreadTokenId implements Serializable {

    private static final long serialVersionUID = 8839088380398635828L;

    private final String value;

    protected static MoneySpreadTokenId from(String value) {

        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("MoneySpreadId value cannot be empty");
        }

        return new MoneySpreadTokenId(value);
    }

    public static MoneySpreadTokenId from(UUID uuid) {
        return MoneySpreadTokenId.from(uuid.toString());
    }

    @Converter(autoApply = true)
    public static class MoneySpreadIdConverter implements AttributeConverter<MoneySpreadTokenId, String> {

        @Override
        public String convertToDatabaseColumn(MoneySpreadTokenId attribute) {

            if (attribute == null) {
                return null;
            }

            return attribute.getValue();
        }

        @Override
        public MoneySpreadTokenId convertToEntityAttribute(String dbData) {

            if (StringUtils.isEmpty(dbData)) {
                return null;
            }

            return MoneySpreadTokenId.from(dbData);
        }
    }
}
