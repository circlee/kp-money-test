package com.kp.test.domain.vo;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Token implements Serializable {

	private static final long serialVersionUID = 8839088380398635828L;
	
	private final String value;
	
	public static Token from(String tokenValue) {
		
		if(StringUtils.isEmpty(tokenValue)) {
			throw new IllegalArgumentException("token value cannot be empty");
		}
		
		return new Token(tokenValue);
	}
}
