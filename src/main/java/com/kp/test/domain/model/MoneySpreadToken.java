package com.kp.test.domain.model;

import com.kp.test.domain.vo.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter(value = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"token", "roomId"}
        , name = "uniqueNameConstraint")}
)
@Entity
@EqualsAndHashCode
public class MoneySpreadToken implements Serializable {

    private static final long serialVersionUID = 7884065933262181778L;

    @Transient
    private static final int DEAULT_TOKEN_AVAILABLE_DAY = 7;

    @Id
    private MoneySpreadTokenId id;

    @Column(nullable = false)
    private Token token;

    @Column(nullable = false)
    private RoomId roomId;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private UserId createUserId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    public static MoneySpreadToken assignCreate(Token token, RoomId roomId, UserId userId) {
        return assignCreate(token, roomId, userId, DEAULT_TOKEN_AVAILABLE_DAY);
    }

    public static MoneySpreadToken assignCreate(Token token, RoomId roomId, UserId userId, int availableDay) {

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiredAt = createdAt.plusDays(availableDay);

        return MoneySpreadToken.builder()
                .id(MoneySpreadTokenId.from(UUID.randomUUID()))
                .token(token)
                .expiredAt(expiredAt)
                .roomId(roomId)
                .createUserId(userId)
                .createdAt(createdAt)
                .build();
    }

    public void assign(RoomId roomId, UserId userId) {
        if (!LocalDateTime.now().isAfter(this.getExpiredAt())) {
            throw new IllegalStateException("expiredAt 이전에 할당 할 수 없습니다.");
        }

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiredAt = createdAt.plusDays(DEAULT_TOKEN_AVAILABLE_DAY);

        this.setRoomId(roomId);
        this.setCreateUserId(userId);
        this.setExpiredAt(expiredAt);
        this.setCreatedAt(createdAt);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.getExpiredAt());
    }

    public MoneySpread creadMoneySpread(RoomId roomId, UserId userId, Price depositPrice, int distributionSize) {
        return MoneySpread.create(
                this.getId()
                , roomId
                , depositPrice
                , distributionSize
                , userId
        );
    }
}
