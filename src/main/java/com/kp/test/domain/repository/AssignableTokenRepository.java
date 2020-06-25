package com.kp.test.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kp.test.domain.model.AssignableToken;
import com.kp.test.domain.vo.Token;

@Repository
public interface AssignableTokenRepository extends JpaRepository<AssignableToken, Token>{

}
