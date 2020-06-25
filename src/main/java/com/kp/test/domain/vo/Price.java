package com.kp.test.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Price implements Serializable {

	private static final long serialVersionUID = 3668749708532705679L;
	
	private final BigDecimal value;
	
	public static Price from(BigDecimal value) {
		
		if(value == null) {
			throw new IllegalArgumentException("price value cannot be empty");
		}
		
		return new Price(value);
	}

	public List<Price> split(PriceSplitter splitter, int size) {
		return splitter.split(this, size);
	}

	public interface PriceSplitter {
		List<Price> split(Price origin, int divideSize);
	}

	@Converter(autoApply = true)
	public static class PriceConverter implements AttributeConverter<Price, BigDecimal> {

		@Override
		public BigDecimal convertToDatabaseColumn(Price attribute) {

			if(attribute == null) {
				return null;
			}

			return attribute.getValue();
		}

		@Override
		public Price convertToEntityAttribute(BigDecimal dbData) {

			if(StringUtils.isEmpty(dbData)) {
				return null;
			}

			return Price.from(dbData);
		}
	}
}
