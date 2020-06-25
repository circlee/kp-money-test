/**
* Copyright WITHINNOVATION Corp. All rights reserved.
*
* 여기 어때 Activity
*/
package com.kp.test.domain.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kp.test.KpMoneySpreadApplication;
import com.kp.test.domain.model.AssignableToken;
import com.kp.test.domain.repository.AssignableTokenRepository;
import com.kp.test.domain.vo.Token;
import com.kp.test.domain.vo.UserId;

import lombok.extern.slf4j.Slf4j;

/**
* @author eldie
*
* AssignableTokenCreatorTest.java
*/
@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ExtendWith(value = {SpringExtension.class})
@SpringBootTest(classes = KpMoneySpreadApplication.class)
public class AssignableTokenCreatorTest {

	@Autowired
	private AssignableTokenRepository assignableTokenRepository;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@Order(0)
	@DisplayName("유저 할당 토큰 생성 테스트")
	@Test
	public void assignCreatTest() {
		
		UserId testUser100 = UserId.from(100L);
		
		AssignableTokenCreator tokenCreator = new AssignableTokenCreator(assignableTokenRepository, tokenGenerator);
		
		AssignableToken assignableToken = tokenCreator.createToken(testUser100);
		
		log.debug("token : {}", assignableToken);
		
		Assertions.assertNotNull(assignableToken);
		Assertions.assertEquals(assignableToken.getAssignedBy(), testUser100);
		
		Optional<AssignableToken> savedAssignableToken = assignableTokenRepository.findById(assignableToken.getToken());
		
		Assertions.assertTrue(savedAssignableToken.isPresent());
		Assertions.assertEquals(assignableToken, savedAssignableToken.get());
		
	
	}
	
	@Order(1)
	@DisplayName("중복 유저 할당 토큰 생성 예외 테스트")
	@Test
	public void duplicateTokenCreateTest() {
		
		UserId testUser100 = UserId.from(100L);
		
		TokenGenerator fixedTokenGenerator = () -> Token.from("FIX");
		
		AssignableTokenCreator tokenCreator = new AssignableTokenCreator(assignableTokenRepository, fixedTokenGenerator);
		
		Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
			tokenCreator.createToken(testUser100);
			tokenCreator.createToken(testUser100);
		});
	}
	
	@Order(2)
	@DisplayName("expiredAt 이전 토큰 재할당 예외 테스트")
	@Test
	public void reAssignErrorTest() {
		
		UserId testUser100 = UserId.from(100L);
		UserId testUser200 = UserId.from(200L);
		
		TokenGenerator fixedTokenGenerator = () -> Token.from("TE1");
		
		AssignableTokenCreator tokenCreator = new AssignableTokenCreator(assignableTokenRepository, fixedTokenGenerator);
		
		AssignableToken assignableToken1 = tokenCreator.createToken(testUser100);
		
		Assertions.assertThrows(IllegalStateException.class, () -> {
			assignableToken1.assign(testUser200);	
		});

	}
}
