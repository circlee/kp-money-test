package com.kp.test.domain.repository;

import com.kp.test.domain.model.MoneySpreadToken;
import com.kp.test.domain.vo.MoneySpreadTokenId;
import com.kp.test.domain.vo.RoomId;
import com.kp.test.domain.vo.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssignableTokenRepository extends JpaRepository<MoneySpreadToken, MoneySpreadTokenId> {

    Optional<MoneySpreadToken> findByTokenAndRoomId(Token token, RoomId roomId);
}
