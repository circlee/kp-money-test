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
public class MoneySpreadDistributionId implements Serializable {

	private static final long serialVersionUID = 8839088380398635828L;
	
	private final String value;

	protected static MoneySpreadDistributionId from(String value) {

		if(StringUtils.isEmpty(value)) {
			throw new IllegalArgumentException("MoneySpreadId value cannot be empty");
		}

		return new MoneySpreadDistributionId(value);
	}

	public static MoneySpreadDistributionId from(UUID uuid) {
		return MoneySpreadDistributionId.from(uuid.toString());
	}

	@Converter(autoApply = true)
	public static class MoneySpreadIdConverter implements AttributeConverter<MoneySpreadDistributionId, String> {

		@Override
		public String convertToDatabaseColumn(MoneySpreadDistributionId attribute) {

			if(attribute == null) {
				return null;
			}

			return attribute.getValue();
		}

		@Override
		public MoneySpreadDistributionId convertToEntityAttribute(String dbData) {

			if(StringUtils.isEmpty(dbData)) {
				return null;
			}

			return MoneySpreadDistributionId.from(dbData);
		}
	}
}
