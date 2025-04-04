package com.linkmoa.source.auth.jwt.refresh.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.linkmoa.source.auth.jwt.refresh.entity.RefreshToken;
import com.linkmoa.source.auth.jwt.refresh.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void saveRefreshToken(String token, String email) {

		refreshTokenRepository.findRefreshTokenByEmail(email)
			.ifPresent(existingToken -> deleteRefreshToken(email));

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

	// TODO : 리프레시 토큰 삭제 스케줄러 추후 수정 필요
	@Scheduled(fixedRate = 1800000, initialDelay = 120000)
	public void deleteExpiredRefreshTokens() {
		refreshTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
	}

}
