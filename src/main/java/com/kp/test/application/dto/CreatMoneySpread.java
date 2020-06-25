package com.kp.test.application.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CreatMoneySpread {

    @DecimalMin(value = "0", inclusive = false)
    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    @NotNull
    private BigDecimal depositPrice;

    @NotNull
    private Integer distributionSize;
}
