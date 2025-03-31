package com.linkmoa.source.auth.jwt.refresh.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.linkmoa.source.auth.jwt.refresh.entity.RefreshToken;
import com.linkmoa.source.auth.jwt.refresh.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	public void saveRefreshToken(String token, String email) {

		if (refreshTokenRepository.existsByEmail(email)) {
			deleteRefreshToken(email);  // 존재할 때만 삭제
		}
		refreshTokenRepository.save(RefreshToken.builder()
			.token(token)
			.email(email)
			.expiresAt(LocalDateTime.now().plusHours(24))
			.build()
		);
	}

	public String getEmailByRefreshToken(String token) {
		return refreshTokenRepository.findRefreshTokenByToken(token)
			.map(RefreshToken::getEmail)
			.orElse(null);

	}

	public void deleteRefreshToken(String email) {
		refreshTokenRepository.deleteByEmail(email);
	}

}
