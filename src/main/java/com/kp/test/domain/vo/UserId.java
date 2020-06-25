package com.kp.test.domain.vo;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserId implements Serializable {

	private static final long serialVersionUID = 3668749708532705679L;
	
	private final Long value;
	
	public static UserId from(Long value) {
		
		if(value == null) {
			throw new IllegalArgumentException("token value cannot be empty");
		}
		
		return new UserId(value);
	}
}
