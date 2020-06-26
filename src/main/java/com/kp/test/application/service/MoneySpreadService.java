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
import com.kp.test.infrastructure.config.exception.KpCustomException;
import com.kp.test.infrastructure.config.exception.KpExceptionCode;
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
                .orElseThrow(() -> new KpCustomException(KpExceptionCode.NOT_AVAILABLE_MONEY_SPREAD));

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
                .orElseThrow(() -> new KpCustomException(KpExceptionCode.NOT_AVAILABLE_MONEY_SPREAD));

        Set<MoneySpreadDistribution> receivedMoneySpreadDistributions = moneySpread.getDistributions().stream()
                .filter(MoneySpreadDistribution::isReceived)
                .collect(Collectors.toSet());

        return MoneySpreadDetail.builder()
                .spreadTime(moneySpread.getCreatedAt())
                .spreadPrice(moneySpread.getDepositPrice().getValue())
                .receivedTotalPrice(moneySpread.getReceivedTotalPrice().getValue())
                .receivers(receivedMoneySpreadDistributions.stream()
                        .map(msd -> MoneySpreadDetail.MoneySpreadDetail_Receiver.builder()
                                .userId(msd.getReceiver().getValue())
                                .receivedPrice(msd.getPrice().getValue())
                                .build())
                        .collect(Collectors.toList())
                )
                .build();
    }

    private MoneySpreadToken getMoneySpreadToken(Token token, RoomId roomId) {

        MoneySpreadToken moneySpreadToken = assignableTokenRepository.findByTokenAndRoomId(token, roomId)
                .filter(at -> !at.isExpired())
                .orElseThrow(() -> new KpCustomException(KpExceptionCode.NOT_AVAILABLE_TOKEN));

        return moneySpreadToken;
    }
}
