/**
* Copyright WITHINNOVATION Corp. All rights reserved.
*
* 여기 어때 Activity
*/
package com.kp.test.domain.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.kp.test.KpMoneySpreadApplication;
import com.kp.test.domain.model.AssignableToken;
import com.kp.test.domain.repository.AssignableTokenRepository;
import com.kp.test.domain.vo.UserId;

import lombok.extern.slf4j.Slf4j;

/**
* @author eldie
*
* AssignableTokenCreatorTest.java
*/
@Slf4j
@SpringBootTest(classes = KpMoneySpreadApplication.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application.yml" })
public class AssignableTokenCreatorTest {

	@Autowired
	private AssignableTokenRepository assignableTokenRepository;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	
	@DisplayName("유저 할당 토큰 생성 테스트")
	@Test
	public void assignCreatTest() {
		
		UserId testUser100 = UserId.from(100L);
		
		AssignableTokenCreator tokenCreator = new AssignableTokenCreator(assignableTokenRepository, tokenGenerator);
		
		AssignableToken assignableToken = tokenCreator.createToken(testUser100);
		
		log.debug("token : {}", assignableToken);
		
		Assertions.assertNotNull(assignableToken);
		Assertions.assertEquals(assignableToken.getAssignedBy(), testUser100);
	}
}
