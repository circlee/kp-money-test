package com.kp.test.domain.service;

import org.springframework.stereotype.Service;

import com.kp.test.domain.model.AssignableToken;
import com.kp.test.domain.repository.AssignableTokenRepository;
import com.kp.test.domain.vo.Token;
import com.kp.test.domain.vo.UserId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AssignableTokenCreator {

	private final AssignableTokenRepository assignableTokenRepository;
	
	private final TokenGenerator tokenGenerator;
	
	public AssignableToken createToken(UserId userId) {
	
		Token token = tokenGenerator.generate();
		
		AssignableToken assignableToken = AssignableToken.assignCreate(token, userId);
		
		assignableTokenRepository.save(assignableToken);
		
		return assignableToken;
	}
}
