package com.linkmoa.source.auth.jwt.refresh.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkmoa.source.auth.jwt.refresh.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
	Optional<RefreshToken> findRefreshTokenByToken(String token);

	void deleteByEmail(String email);

	boolean existsByEmail(String email);

	void deleteByExpiresAtBefore(LocalDateTime time);

	Optional<RefreshToken> findRefreshTokensByEmail(String email);
}
