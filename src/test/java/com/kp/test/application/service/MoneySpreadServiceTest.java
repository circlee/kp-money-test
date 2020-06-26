package com.kp.test.application.service;

import com.kp.test.KpMoneySpreadApplication;
import com.kp.test.application.dto.CreatMoneySpread;
import com.kp.test.application.dto.MoneySpreadDetail;
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
        CreatMoneySpread creatMoneySpread = new CreatMoneySpread(new BigDecimal("20000"), 15);

        Token token = moneySpreadService.createMoneySpreadInRoom(roomId, userId, creatMoneySpread);

        Assertions.assertNotNull(token);
    }

    @Order(2)
    @DisplayName("뿌리기 받기 테스트")
    @Test
    public void receiveTest() {

        Long roomId = 10L;
        Long createUserId = 123L;
        CreatMoneySpread creatMoneySpread = new CreatMoneySpread(new BigDecimal("20000"), 15);

        Token token = moneySpreadService.createMoneySpreadInRoom(roomId, createUserId, creatMoneySpread);


        Long receiveUserId = 400L;

        moneySpreadService.receiveMoneySpread(token.getValue(), roomId, receiveUserId);

    }

    @Order(2)
    @DisplayName("뿌리기 받기 , 생성자 받기 예외테스트")
    @Test
    public void receiveTest2() {

        Long roomId = 10L;
        Long createUserId = 123L;
        CreatMoneySpread creatMoneySpread = new CreatMoneySpread(new BigDecimal("20000"), 15);

        Token token = moneySpreadService.createMoneySpreadInRoom(roomId, createUserId, creatMoneySpread);


        Long receiveUserId = createUserId;

        Assertions.assertThrows(Exception.class, ()-> {
            moneySpreadService.receiveMoneySpread(token.getValue(), roomId, receiveUserId);
        });
    }

    @Order(2)
    @DisplayName("뿌리기 조회 테스트")
    @Test
    public void showMoneySpreadTest() {

        Long roomId = 10L;
        Long createUserId = 123L;
        CreatMoneySpread creatMoneySpread = new CreatMoneySpread(new BigDecimal("20000"), 15);

        Token token = moneySpreadService.createMoneySpreadInRoom(roomId, createUserId, creatMoneySpread);


        Long receiveUserId = 400L;

        moneySpreadService.receiveMoneySpread(token.getValue(), roomId, receiveUserId);

        MoneySpreadDetail detail = moneySpreadService.getMoneySpreadDetail(token.getValue(), roomId, createUserId);

        Assertions.assertEquals(detail.getReceivers().size(), 1);
    }

    @Order(2)
    @DisplayName("뿌리기 조회, 생성자 x 예외 테스트")
    @Test
    public void showMoneySpreadTest2() {

        Long roomId = 10L;
        Long createUserId = 123L;
        CreatMoneySpread creatMoneySpread = new CreatMoneySpread(new BigDecimal("20000"), 15);

        Token token = moneySpreadService.createMoneySpreadInRoom(roomId, createUserId, creatMoneySpread);


        Long receiveUserId = 400L;

        moneySpreadService.receiveMoneySpread(token.getValue(), roomId, receiveUserId);

        Assertions.assertThrows(RuntimeException.class, () -> {
            moneySpreadService.getMoneySpreadDetail(token.getValue(), roomId, receiveUserId);
        });
    }
}
