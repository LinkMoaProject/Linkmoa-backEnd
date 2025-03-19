package com.linkmoa.source.auth.jwt.refresh.repository;

import com.linkmoa.source.auth.jwt.refresh.entity.RefreshToken;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
