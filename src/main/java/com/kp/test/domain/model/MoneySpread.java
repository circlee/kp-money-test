package com.kp.test.domain.model;

import com.kp.test.domain.vo.*;
import com.kp.test.infrastructure.util.DecimalPriceSplitter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@EqualsAndHashCode
public class MoneySpread {

    @Transient
    private static final int MONEY_SPREAD_ACTIVE_MINUTE = 10;

    @Id
    private MoneySpreadId id;

    @Column(nullable = false)
    private RoomId roomId;

    @Column(nullable = false)
    private Token token;

    @Column(nullable = false)
    private Price depositPrice;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private UserId createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany
    @JoinColumn(name = "money_spread_id")
    private Set<MoneySpreadDistribution> distributions;

    @Version
    private Long version;

    public static MoneySpread create(RoomId roomId, Token token, Price depositPrice, int distributionSize, UserId userId) {

        if(distributionSize <= 0) {
            throw new IllegalArgumentException("distributionSize 는 0 보타 작을수 없습니다");
        }

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiredAt = createdAt.plusMinutes(MONEY_SPREAD_ACTIVE_MINUTE);

        return MoneySpread.builder()
                .id(MoneySpreadId.from(UUID.randomUUID()))
                .roomId(roomId)
                .token(token)
                .depositPrice(depositPrice)
                .expiredAt(expiredAt)
                .createdBy(userId)
                .createdAt(createdAt)
                .distributions(priceSplitDistribute(depositPrice, distributionSize))
                .build();
    }

    private static Set<MoneySpreadDistribution> priceSplitDistribute(Price depositPrice, int distributionSize) {

        List<Price> prices = depositPrice.split(DecimalPriceSplitter.instance(), distributionSize);

        return prices.stream()
                .map(MoneySpreadDistribution::from)
                .collect(Collectors.toSet());
    }



}
