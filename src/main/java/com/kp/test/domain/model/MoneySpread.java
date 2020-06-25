package com.kp.test.domain.model;

import com.kp.test.domain.vo.*;
import com.kp.test.infrastructure.util.DecimalPriceSplitter;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
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
    private MoneySpreadTokenId moneySpreadTokenId;

    @Column(nullable = false)
    private Price depositPrice;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private UserId createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "money_spread_id")
    private Set<MoneySpreadDistribution> distributions;

    @Column(nullable = false)
    private Price receivedTotalPrice;

    @Version
    private Long version;

    public static MoneySpread create(MoneySpreadTokenId moneySpreadTokenId, RoomId roomId, Price depositPrice, int distributionSize, UserId userId) {

        if (distributionSize <= 0) {
            throw new IllegalArgumentException("distributionSize 는 0 보타 작을수 없습니다");
        }

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiredAt = createdAt.plusMinutes(MONEY_SPREAD_ACTIVE_MINUTE);

        return MoneySpread.builder()
                .id(MoneySpreadId.from(UUID.randomUUID()))
                .roomId(roomId)
                .moneySpreadTokenId(moneySpreadTokenId)
                .depositPrice(depositPrice)
                .expiredAt(expiredAt)
                .createdBy(userId)
                .createdAt(createdAt)
                .distributions(priceSplitDistribute(depositPrice, distributionSize))
                .receivedTotalPrice(Price.from(BigDecimal.ZERO))
                .build();
    }

    private static Set<MoneySpreadDistribution> priceSplitDistribute(Price depositPrice, int distributionSize) {

        List<Price> prices = depositPrice.split(DecimalPriceSplitter.instance(), distributionSize);

        return prices.stream()
                .map(MoneySpreadDistribution::from)
                .collect(Collectors.toSet());
    }

    public void receiveDistribution(UserId userId) {
        if(this.createdBy.equals(userId)) {
            throw new IllegalArgumentException("생성자는 받을 수 없습니다.");
        }

        MoneySpreadDistribution remainDistribution = this.getDistributions().stream()
                .filter(msd -> !msd.isReceived())
                .filter(msd -> !isExpired())
                .findAny()
                .orElseThrow(() -> new RuntimeException("no remain..."));

        remainDistribution.receive(userId);

        this.setReceivedTotalPrice(this.getReceivedTotalPrice().add(remainDistribution.getPrice()));
    }


    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.getExpiredAt());
    }
}
