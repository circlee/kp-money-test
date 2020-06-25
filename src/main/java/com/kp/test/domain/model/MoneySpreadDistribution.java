package com.kp.test.domain.model;

import com.kp.test.domain.vo.MoneySpreadDistributionId;
import com.kp.test.domain.vo.Price;
import com.kp.test.domain.vo.UserId;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@EqualsAndHashCode
public class MoneySpreadDistribution {

    @Id
    private MoneySpreadDistributionId id;

    @Column(nullable = false)
    private Price price;

    @Column
    private UserId receiver;

    @Column
    private LocalDateTime receivedAt;

    @Version
    private Long version;

    public static MoneySpreadDistribution from(Price price) {
        return MoneySpreadDistribution.builder()
                .id(MoneySpreadDistributionId.from(UUID.randomUUID()))
                .price(price)
                .build();
    }

    public void receive(UserId receiver) {

        if(this.getReceiver() != null) {
            throw new IllegalStateException("이미 수신된 금액을 수신할수 없습니다.");
        }

        if(this.getReceivedAt() != null) {
            throw new IllegalStateException("이미 수신된 금액을 수신할수 없습니다.");
        }

        this.setReceiver(receiver);
        this.setReceivedAt(LocalDateTime.now());
    }

}
