package com.kp.test.domain.service;

import com.kp.test.domain.model.MoneySpread;
import com.kp.test.domain.model.MoneySpreadToken;
import com.kp.test.domain.repository.MoneySpreadRepository;
import com.kp.test.infrastructure.config.exception.KpCustomException;
import com.kp.test.infrastructure.config.exception.KpExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MoneySpreadManager {

    private final MoneySpreadRepository moneySpreadRepository;

    public MoneySpread getActiveMoneySpread(MoneySpreadToken moneySpreadToken) {
        return moneySpreadRepository.findByMoneySpreadTokenId(moneySpreadToken.getId())
                .filter(ms -> !ms.isExpired())
                .orElseThrow(() -> new KpCustomException(KpExceptionCode.NOT_ACCEPTABLE_MONEY_SPREAD));
    }

}
