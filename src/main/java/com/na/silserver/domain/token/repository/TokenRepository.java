package com.na.silserver.domain.token.repository;

import com.na.silserver.domain.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

	// 메서드 확인(키값등 재설계)
	
	Boolean existsByRefreshToken(String refresh);
    @Transactional
	void deleteByUsername(String username);
    @Transactional
	void deleteByRefreshToken(String refresh);
	
}
