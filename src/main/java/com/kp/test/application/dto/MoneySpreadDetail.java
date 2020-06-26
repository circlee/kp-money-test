package com.kp.test.application.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class MoneySpreadDetail implements Serializable {

    private LocalDateTime spreadTime;

    private BigDecimal spreadPrice;

    private BigDecimal receivedTotalPrice;

    private List<MoneySpreadDetail_Receiver> receivers;


    @Getter
    @Builder
    @EqualsAndHashCode
    @ToString
    public static class MoneySpreadDetail_Receiver {
        private BigDecimal receivedPrice;

        private Long userId;
    }
}
