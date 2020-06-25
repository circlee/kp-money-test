package com.kp.test.domain.vo.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.util.StringUtils;

import com.kp.test.domain.vo.Token;


@Converter(autoApply = true)
public class TokenConverter implements AttributeConverter<Token, String>{

	@Override
	public String convertToDatabaseColumn(Token attribute) {
		
		if(attribute == null) {
			return null;
		}
		
		return attribute.getValue();
	}

	@Override
	public Token convertToEntityAttribute(String dbData) {
		
		if(StringUtils.isEmpty(dbData)) {
			return null;
		}
		
		return Token.from(dbData);
	}

}
