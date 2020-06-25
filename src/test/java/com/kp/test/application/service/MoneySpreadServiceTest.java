package com.kp.test.application.service;

import com.kp.test.KpMoneySpreadApplication;
import com.kp.test.application.dto.CreatMoneySpread;
import com.kp.test.domain.vo.Token;
import lombok.extern.slf4j.Slf4j;
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
public class MoneySpreadServiceTest {

    @Autowired
    private MoneySpreadService moneySpreadService;

    @Order(1)
    @DisplayName("뿌리기 서비스 호출 테스트")
    @Test
    public void createTest() {

        Long roomId = 10L;
        Long userId = 123L;
        CreatMoneySpread creatMoneySpread = new CreatMoneySpread();
        creatMoneySpread.setDepositPrice(new BigDecimal("20000"));
        creatMoneySpread.setDistributionSize(15);

        Token token = moneySpreadService.createMoneySpreadInRoom(roomId, userId, creatMoneySpread);

        Assertions.assertNotNull(token);
    }
}
