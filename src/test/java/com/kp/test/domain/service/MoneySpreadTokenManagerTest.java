
package com.kp.test.domain.service;

import com.kp.test.KpMoneySpreadApplication;
import com.kp.test.domain.model.MoneySpreadToken;
import com.kp.test.domain.repository.AssignableTokenRepository;
import com.kp.test.domain.vo.RoomId;
import com.kp.test.domain.vo.Token;
import com.kp.test.domain.vo.UserId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;


@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ExtendWith(value = {SpringExtension.class})
@SpringBootTest(classes = KpMoneySpreadApplication.class)
class MoneySpreadTokenManagerTest {

	@Autowired
	private AssignableTokenRepository assignableTokenRepository;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@Order(0)
	@DisplayName("유저 할당 토큰 생성 테스트")
	@Test
	void assignCreatTest() {

		RoomId roomId = RoomId.from(1L);
		UserId testUser100 = UserId.from(100L);
		
		AssignableTokenManager tokenCreator = new AssignableTokenManager(assignableTokenRepository, tokenGenerator);
		
		MoneySpreadToken moneySpreadToken = tokenCreator.createToken(roomId, testUser100);
		
		log.debug("token : {}", moneySpreadToken);
		
		Assertions.assertNotNull(moneySpreadToken);
		Assertions.assertEquals(moneySpreadToken.getCreateUserId(), testUser100);
		
		Optional<MoneySpreadToken> savedAssignableToken = assignableTokenRepository.findById(moneySpreadToken.getId());
		
		Assertions.assertTrue(savedAssignableToken.isPresent());
		Assertions.assertEquals(moneySpreadToken, savedAssignableToken.get());
		
	
	}
	
	@Order(1)
	@DisplayName("중복 유저 할당 토큰 생성 예외 테스트")
	@Test
	void duplicateTokenCreateTest() {

		RoomId roomId = RoomId.from(1L);
		UserId testUser100 = UserId.from(100L);
		
		TokenGenerator fixedTokenGenerator = () -> Token.from("FIX");
		
		AssignableTokenManager tokenCreator = new AssignableTokenManager(assignableTokenRepository, fixedTokenGenerator);
		
		Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
			tokenCreator.createToken(roomId, testUser100);
			tokenCreator.createToken(roomId, testUser100);
		});
	}
	
	@Order(2)
	@DisplayName("expiredAt 이전 토큰 재할당 예외 테스트")
	@Test
	void reAssignErrorTest() {

		RoomId roomId1 = RoomId.from(1L);
		RoomId roomId2 = RoomId.from(1L);
		UserId testUser100 = UserId.from(100L);
		UserId testUser200 = UserId.from(200L);
		
		TokenGenerator fixedTokenGenerator = () -> Token.from("TE1");
		
		AssignableTokenManager tokenCreator = new AssignableTokenManager(assignableTokenRepository, fixedTokenGenerator);
		
		MoneySpreadToken moneySpreadToken1 = tokenCreator.createToken(roomId1, testUser100);
		
		Assertions.assertThrows(IllegalStateException.class, () -> moneySpreadToken1.assign(roomId2, testUser200));
	}

	@Order(3)
	@DisplayName("expiredAt 이후 토큰 재할당 테스트")
	@Test
	void reAssignAfterExpiredTest() throws InterruptedException {

		RoomId roomId1 = RoomId.from(1L);
		RoomId roomId2 = RoomId.from(1L);
		UserId testUser100 = UserId.from(100L);
		UserId testUser200 = UserId.from(200L);

		TokenGenerator fixedTokenGenerator = () -> Token.from("TE2");

		AssignableTokenManager tokenCreator = new AssignableTokenManager(assignableTokenRepository, fixedTokenGenerator);

		MoneySpreadToken moneySpreadToken1 = tokenCreator.createToken(roomId1, testUser100, 0);

		moneySpreadToken1.assign(roomId2, testUser200);
	}

}
