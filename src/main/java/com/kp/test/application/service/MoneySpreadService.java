package com.kp.test.application.service;

import com.kp.test.application.dto.CreatMoneySpread;
import com.kp.test.application.dto.MoneySpreadDetail;
import com.kp.test.domain.model.MoneySpreadToken;
import com.kp.test.domain.model.MoneySpread;
import com.kp.test.domain.model.MoneySpreadDistribution;
import com.kp.test.domain.repository.AssignableTokenRepository;
import com.kp.test.domain.repository.MoneySpreadRepository;
import com.kp.test.domain.service.AssignableTokenCreator;
import com.kp.test.domain.vo.Price;
import com.kp.test.domain.vo.RoomId;
import com.kp.test.domain.vo.Token;
import com.kp.test.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MoneySpreadService {

    private final AssignableTokenRepository assignableTokenRepository;

    private final AssignableTokenCreator assignableTokenCreator;

    private final MoneySpreadRepository moneySpreadRepository;

    @Transactional
    public Token createMoneySpreadInRoom(Long aRoomId, Long anUserId, CreatMoneySpread creatMoneySpread) {

        RoomId roomId = RoomId.from(aRoomId);
        UserId userId = UserId.from(anUserId);

        MoneySpreadToken moneySpreadToken = assignableTokenCreator.createToken(roomId, userId);

        MoneySpread moneySpread = MoneySpread.create(
                moneySpreadToken.getId()
                , roomId
                , Price.from(creatMoneySpread.getDepositPrice())
                , creatMoneySpread.getDistributionSize()
                , userId
        );

        moneySpreadRepository.saveAndFlush(moneySpread);

        return moneySpreadToken.getToken();
    }

    @Transactional
    public void receiveMoneySpread(String tokenValue, Long aRoomId, Long aUserId) {

        Token token = Token.from(tokenValue);
        RoomId roomId = RoomId.from(aRoomId);
        UserId userId = UserId.from(aUserId);

        MoneySpreadToken moneySpreadToken = getMoneySpreadToken(token, roomId);

        MoneySpread moneySpread = moneySpreadRepository.findByMoneySpreadTokenId(moneySpreadToken.getId())
                .filter(ms -> !ms.isExpired())
                .orElseThrow(() -> new RuntimeException(""));

        moneySpread.receiveDistribution(userId);
    }

    @Transactional(readOnly = true)
    public MoneySpreadDetail getMoneySpreadDetail(String tokenValue, Long aRoomId, Long aUserId) {

        Token token = Token.from(tokenValue);
        RoomId roomId = RoomId.from(aRoomId);
        UserId userId = UserId.from(aUserId);

        MoneySpreadToken moneySpreadToken = getMoneySpreadToken(token, roomId);

        MoneySpread moneySpread = moneySpreadRepository.findByMoneySpreadTokenId(moneySpreadToken.getId())
                .filter(ms -> ms.getCreatedBy().equals(userId))
                .filter(ms -> !ms.isExpired())
                .orElseThrow(() -> new RuntimeException(""));

        Set<MoneySpreadDistribution> receivedMoneySpreadDistributions = moneySpread.getDistributions().stream()
                .filter(MoneySpreadDistribution::isReceived)
                .collect(Collectors.toSet());


//        뿌린시각,뿌린금액,받기완료된금액,받기완료된정보([받은금액,받은
//        사용자 아이디] 리스트)

        return null;
    }

    private MoneySpreadToken getMoneySpreadToken(Token token, RoomId roomId) {

        MoneySpreadToken moneySpreadToken = assignableTokenRepository.findByTokenAndRoomId(token, roomId)
                .filter(at -> !at.isExpired())
                .orElseThrow(() -> new RuntimeException(""));

        return moneySpreadToken;
    }
}
