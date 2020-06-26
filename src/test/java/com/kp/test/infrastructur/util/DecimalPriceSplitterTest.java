package com.kp.test.infrastructur.util;

import com.kp.test.domain.vo.Price;
import com.kp.test.infrastructure.util.DecimalPriceSplitter;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class DecimalPriceSplitterTest {

    private static DecimalPriceSplitter decimalPriceSplitter = DecimalPriceSplitter.instance();

    @Order(1)
    @DisplayName("20 / 3 균등 분배 테스트 ")
    @Test
    public void test1(){

        Price origin = Price.from(new BigDecimal("20"));

        List<Price> prices = origin.split(decimalPriceSplitter , 3);

        Assertions.assertNotNull(prices);
        Assertions.assertEquals(prices.size(), 3);
        org.assertj.core.api.Assertions.assertThat(prices)
                .containsExactly(Price.from(new BigDecimal("7"))
                        ,Price.from(new BigDecimal("7"))
                        ,Price.from(new BigDecimal("6"))
                );
    }

    @Order(2)
    @DisplayName("10 / 15 최소 분배 예외 테스트 ")
    @Test
    public void test2(){

        Price origin = Price.from(new BigDecimal("10"));

        Assertions.assertThrows(UnsupportedOperationException.class, ()-> {
            origin.split(decimalPriceSplitter , 15);
        });
    }


}
