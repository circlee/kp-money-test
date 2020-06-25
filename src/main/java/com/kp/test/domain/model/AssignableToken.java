package com.kp.test.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.kp.test.domain.vo.Token;
import com.kp.test.domain.vo.UserId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class AssignableToken implements Serializable {
	
	private static final long serialVersionUID = 7884065933262181778L;

	@Transient
	private static final int TOKEN_ACTIVE_DAY = 7;

	@Id
	private Token token;
	
	private LocalDateTime expiredAt;
	
	private UserId assignedBy;
	
	private LocalDateTime assignedAt;
	
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
}
