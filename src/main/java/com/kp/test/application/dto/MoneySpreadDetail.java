package com.kp.test.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MoneySpreadDetail implements Serializable {
    //        뿌린시각,뿌린금액,받기완료된금액,받기완료된정보([받은금액,받은
//        사용자 아이디] 리스트)

    private LocalDateTime spreadTime;

    private BigDecimal spreadPrice;

    private BigDecimal receivedTotalPrice;

    private List<MoneySpreadDetail_Receiver> receivers;

    public static class MoneySpreadDetail_Receiver {
        private BigDecimal receivedPrice;

        private Long userId;
    }
}
