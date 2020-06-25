package com.kp.test.domain.vo.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.util.StringUtils;

import com.kp.test.domain.vo.UserId;


@Converter(autoApply = true)
public class UserIdConverter implements AttributeConverter<UserId, Long>{

	@Override
	public Long convertToDatabaseColumn(UserId attribute) {
		
		if(attribute == null) {
			return null;
		}
		
		return attribute.getValue();
	}

	@Override
	public UserId convertToEntityAttribute(Long dbData) {
		
		if(StringUtils.isEmpty(dbData)) {
			return null;
		}
		
		return UserId.from(dbData);
	}

}
