package com.kp.test.domain.service;

import com.kp.test.domain.model.MoneySpreadToken;
import com.kp.test.domain.repository.AssignableTokenRepository;
import com.kp.test.domain.vo.RoomId;
import com.kp.test.domain.vo.Token;
import com.kp.test.domain.vo.UserId;
import com.kp.test.infrastructure.config.exception.KpCustomException;
import com.kp.test.infrastructure.config.exception.KpExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AssignableTokenManager {

    private final AssignableTokenRepository assignableTokenRepository;

    private final TokenGenerator tokenGenerator;

    public MoneySpreadToken createToken(RoomId roomId, UserId userId) {

        Token token = tokenGenerator.generate();

        MoneySpreadToken moneySpreadToken = MoneySpreadToken.assignCreate(token, roomId, userId);

        assignableTokenRepository.save(moneySpreadToken);

        return moneySpreadToken;
    }

    public MoneySpreadToken createToken(RoomId roomId, UserId userId, int customAvailableDay) {

        Token token = tokenGenerator.generate();

        MoneySpreadToken moneySpreadToken = MoneySpreadToken.assignCreate(token, roomId, userId, customAvailableDay);

        assignableTokenRepository.save(moneySpreadToken);

        return moneySpreadToken;
    }

    public MoneySpreadToken getValidMoneySpreadToken(Token token, RoomId roomId) {

        MoneySpreadToken moneySpreadToken = assignableTokenRepository.findByTokenAndRoomId(token, roomId)
                .filter(at -> !at.isExpired())
                .orElseThrow(() -> new KpCustomException(KpExceptionCode.NOT_AVAILABLE_TOKEN));

        return moneySpreadToken;
    }
}
