package com.linkmoa.source.auth.jwt.refresh.service;

import com.linkmoa.source.auth.jwt.refresh.entity.RefreshToken;
import com.linkmoa.source.auth.jwt.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(String refreshToken, String email) {
        refreshTokenRepository.save(RefreshToken.builder()
                .refreshToken(refreshToken)
                .memberEmail(email)
                .build()
        );

        // memberEmail을 key로, refreshToken 을 value 저장
        redisTemplate.opsForValue().set(email, refreshToken, 86400, TimeUnit.SECONDS);

        log.info("흠 : {} ", getEmailByRefreshToken(refreshToken));
        log.info("흠 : {} ", getRefreshTokenByEmail(email));
    }

    public String getRefreshTokenByEmail(String email) {
        return (String) redisTemplate.opsForValue().get(email);
    }

    public String getEmailByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(RefreshToken::getMemberEmail)
                .orElse(null);
    }


    public void deleteRefreshToken(String refreshToken) {
        String email = getEmailByRefreshToken(refreshToken);
        if (email != null) {
            refreshTokenRepository.deleteById(email);
            redisTemplate.delete(refreshToken);
        }
    }
}
