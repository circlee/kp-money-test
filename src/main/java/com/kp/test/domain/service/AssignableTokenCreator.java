package com.kp.test.domain.service;

import com.kp.test.domain.model.MoneySpreadToken;
import com.kp.test.domain.repository.AssignableTokenRepository;
import com.kp.test.domain.vo.RoomId;
import com.kp.test.domain.vo.Token;
import com.kp.test.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AssignableTokenCreator {

    private final AssignableTokenRepository assignableTokenRepository;

    private final TokenGenerator tokenGenerator;

    public MoneySpreadToken createToken(RoomId roomId, UserId userId) {

        Token token = tokenGenerator.generate();

        MoneySpreadToken moneySpreadToken = MoneySpreadToken.assignCreate(token, roomId, userId);

        assignableTokenRepository.save(moneySpreadToken);

        return moneySpreadToken;
    }
}
