package com.kp.test.domain.model;

import com.kp.test.domain.vo.Token;
import com.kp.test.domain.vo.UserId;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter(value = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@EqualsAndHashCode
public class AssignableToken implements Serializable {
	
	private static final long serialVersionUID = 7884065933262181778L;

	@Transient
	private static final int TOKEN_ACTIVE_DAY = 7;

	@Id
	private Token token;

	@Column(nullable = false)
	private LocalDateTime expiredAt;

	@Column(nullable = false)
	private UserId assignedBy;

	@Column(nullable = false)
	private LocalDateTime assignedAt;
	
	@Version
	private Long version;
	
	public static AssignableToken assignCreate(Token token, UserId userId) {
		
		LocalDateTime assignedAt = LocalDateTime.now();
		LocalDateTime expiredAt = assignedAt.plusDays(TOKEN_ACTIVE_DAY);
		
		return AssignableToken.builder()
				.token(token)
				.expiredAt(expiredAt)
				.assignedBy(userId)
				.assignedAt(assignedAt)
				.build();
	}
	
	public void assign(UserId userId) {
		if(!LocalDateTime.now().isAfter(this.getExpiredAt())) {
			throw new IllegalStateException("expiredAt 이전에 할당 할 수 없습니다.");
		}
		
		LocalDateTime assignedAt = LocalDateTime.now();
		LocalDateTime expiredAt = assignedAt.plusDays(TOKEN_ACTIVE_DAY);
		
		this.setExpiredAt(expiredAt);
		this.setAssignedBy(userId);
		this.setAssignedAt(assignedAt);
	}
}
