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
public class MoneySpreadId implements Serializable {

	private static final long serialVersionUID = 8839088380398635828L;
	
	private final String value;

	protected static MoneySpreadId from(String value) {

		if(StringUtils.isEmpty(value)) {
			throw new IllegalArgumentException("MoneySpreadId value cannot be empty");
		}

		return new MoneySpreadId(value);
	}

	public static MoneySpreadId from(UUID uuid) {
		return MoneySpreadId.from(uuid.toString());
	}

	@Converter(autoApply = true)
	public static class MoneySpreadIdConverter implements AttributeConverter<MoneySpreadId, String> {

		@Override
		public String convertToDatabaseColumn(MoneySpreadId attribute) {

			if(attribute == null) {
				return null;
			}

			return attribute.getValue();
		}

		@Override
		public MoneySpreadId convertToEntityAttribute(String dbData) {

			if(StringUtils.isEmpty(dbData)) {
				return null;
			}

			return MoneySpreadId.from(dbData);
		}
	}
}
