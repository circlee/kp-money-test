/**
* Copyright WITHINNOVATION Corp. All rights reserved.
*
* 여기 어때 Activity
*/
package com.kp.test.infrastructure.service;

import org.springframework.stereotype.Component;

import com.kp.test.domain.service.TokenGenerator;
import com.kp.test.domain.vo.Token;

/**
* @author eldie
*
* TokenRandomGenerator.java
*/
@Component
public class TokenRandomGenerator implements TokenGenerator {

	@Override
	public Token generate() {
		return Token.from("AAA");
	}

}
