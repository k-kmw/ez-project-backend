package com.youngcha.ez.global.security.domain.repository;

import com.youngcha.ez.global.security.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByTokenValue(String tokenValue);

    void deleteByTokenValue(String tokenValue);
}
