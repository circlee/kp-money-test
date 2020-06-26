package com.kp.test.domain.repository;

import com.kp.test.KpMoneySpreadApplication;
import com.kp.test.domain.model.MoneySpreadToken;
import com.kp.test.domain.model.MoneySpread;
import com.kp.test.domain.service.AssignableTokenManager;
import com.kp.test.domain.service.TokenGenerator;
import com.kp.test.domain.vo.Price;
import com.kp.test.domain.vo.RoomId;
import com.kp.test.domain.vo.UserId;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ExtendWith(value = {SpringExtension.class})
@SpringBootTest(classes = KpMoneySpreadApplication.class)
public class MoneySpreadRepositoryTest {

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private MoneySpreadRepository moneySpreadRepository;

    @Autowired
    private AssignableTokenManager assignableTokenManager;

    public MoneySpread getMoneySpread(int distributionSize) {
        RoomId roomId = RoomId.from(100L);
        UserId userId = UserId.from(100L);
        MoneySpreadToken moneySpreadToken = assignableTokenManager.createToken(roomId, userId);

        return moneySpreadToken.creadMoneySpread(
                RoomId.from(100L)
                , UserId.from(100L)
                , Price.from(BigDecimal.valueOf(100L))
                , distributionSize
                );

    }

    @Order(1)
    @DisplayName("뿌리기 모델 생성 저장 테스트")
    @Test
    public void createModelTest() {
        MoneySpread moneySpread = getMoneySpread(8);
        Assertions.assertThat(moneySpread)
                .isNotNull();
        moneySpreadRepository.save(moneySpread);
    }
}
