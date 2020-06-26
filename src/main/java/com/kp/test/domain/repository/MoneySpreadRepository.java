package com.kp.test.domain.repository;

import com.kp.test.domain.model.MoneySpread;
import com.kp.test.domain.vo.MoneySpreadTokenId;
import com.kp.test.domain.vo.MoneySpreadId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface MoneySpreadRepository extends JpaRepository<MoneySpread, MoneySpreadId> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<MoneySpread> findByMoneySpreadTokenId(MoneySpreadTokenId moneySpreadTokenId);

}
