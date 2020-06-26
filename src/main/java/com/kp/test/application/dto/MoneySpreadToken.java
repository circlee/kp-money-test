package com.kp.test.application.dto;

import com.kp.test.domain.vo.Token;
import lombok.*;

import java.io.Serializable;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class MoneySpreadToken implements Serializable {

    private String token;

    public static MoneySpreadToken from(Token token) {
        return new MoneySpreadToken(token.getValue());
    }

}
