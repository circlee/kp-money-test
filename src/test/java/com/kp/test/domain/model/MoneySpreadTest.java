package com.kp.test.domain.model;

import com.kp.test.domain.vo.Price;
import com.kp.test.domain.vo.RoomId;
import com.kp.test.domain.vo.Token;
import com.kp.test.domain.vo.UserId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class MoneySpreadTest {

    public MoneySpread getMoneySpread(int distributionSize) {
        return MoneySpread.create(RoomId.from(100L)
                , Token.from("test")
                , Price.from(BigDecimal.valueOf(100L))
                , distributionSize
                , UserId.from(100L)
        );
    }

    @Order(1)
    @DisplayName("뿌리기 모델 생성 테스트")
    @Test
    public void createModelTest() {
        MoneySpread moneySpread = getMoneySpread(8);
        Assertions.assertThat(moneySpread)
                .isNotNull();
    }

    @Order(2)
    @DisplayName("뿌리기 모델 분할 테스트")
    @Test
    public void modelPriceDistributionTest(){
        final int distributionSize = 8;
        MoneySpread moneySpread = getMoneySpread(distributionSize);
        Assertions.assertThat(moneySpread.getDistributions().size())
                .isEqualTo(distributionSize);
    }

    @Order(3)
    @DisplayName("뿌리기 모델 분할 예외 테스트")
    @Test
    public void modelPriceDistributionErrorTest(){
        final int distributionSize = 101;
        Assertions.assertThatThrownBy(() -> getMoneySpread(distributionSize))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
