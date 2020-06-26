package com.kp.test.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
public class CreatMoneySpread {

    @DecimalMin(value = "0", inclusive = false)
    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    @NotNull
    private final BigDecimal depositPrice;

    @NotNull
    private final Integer distributionSize;

    @JsonCreator
    public CreatMoneySpread(@JsonProperty("depoitPrice") BigDecimal depositPrice
            , @JsonProperty("distributionSize") Integer distributionSize) {

        this.depositPrice = depositPrice;
        this.distributionSize = distributionSize;
    }
}
