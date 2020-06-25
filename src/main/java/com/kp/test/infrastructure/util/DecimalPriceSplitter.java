package com.kp.test.infrastructure.util;

import com.kp.test.domain.vo.Price;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecimalPriceSplitter implements Price.PriceSplitter {

    private static int SCALE = 0;

    private DecimalPriceSplitter() {
    }

    private static class DecimalPriceSplitterHolder {
        private static DecimalPriceSplitter INSTANCE = new DecimalPriceSplitter();
    }

    public static DecimalPriceSplitter instance() {
        return DecimalPriceSplitterHolder.INSTANCE;
    }

    @Override
    public List<Price> split(Price origin, int divideSize) {

        BigDecimal originValue = origin.getValue();

        BigDecimal perRoundDownValue = originValue.divide(BigDecimal.valueOf(divideSize), SCALE, RoundingMode.DOWN);

        if (perRoundDownValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new UnsupportedOperationException("0 이하로 나눠질수 없습니다");
        }

        BigDecimal remainValue = originValue.subtract(perRoundDownValue.multiply(BigDecimal.valueOf(divideSize)));

        AtomicInteger remainOneCount = new AtomicInteger(remainValue.divide(BigDecimal.ONE).intValue());
        return IntStream.range(0, divideSize)
                .mapToObj(i -> {
                    Integer remainOneCountValue = remainOneCount.getAndDecrement();
                    if (remainOneCountValue.compareTo(0) > 0) {
                        return perRoundDownValue.add(BigDecimal.ONE);
                    }
                    return perRoundDownValue;
                })
                .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
                .map(Price::from)
                .collect(Collectors.toList());

    }


}
