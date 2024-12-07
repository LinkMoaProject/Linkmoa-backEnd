package com.linkmoa.source.auth.jwt.provider;

import com.linkmoa.source.domain.member.exception.JwtTokenException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.linkmoa.source.domain.member.error.JwtErrorCode.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    @Getter
    private SecretKey secretKey;

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.access-token-expiration}")
    private Long ACCESS_TOKEN_EXPIRATION;

    @Value("${spring.jwt.refresh-token-expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;

    @PostConstruct
    private void init() {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createAccessToken(String email, String role) {
        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .claim("token_type", "access_token")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .claim("token_type", "refresh_token")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) throws JwtTokenException {
        try {
            Jwts.parser()
                    .verifyWith(secretKey) // 서명 검증
                    .build()
                    .parseSignedClaims(token); // JWT 파싱
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new JwtTokenException(NOT_VALID_TOKEN_EXCEPTION); // 서명이 유효하지 않음
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            if (isExpired(token)) {
                throw new JwtTokenException(EXPIRED_REFRESH_TOKEN_EXCEPTION); // Refresh Token 만료
            }
            throw new JwtTokenException(EXPIRED_ACCESS_TOKEN_EXCEPTION); // Access Token 만료
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new JwtTokenException(UNSUPPORTED_TOKEN_EXCEPTION); // 지원되지 않는 토큰
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new JwtTokenException(MISMATCH_CLAIMS_EXCEPTION); // 클레임이 비었음
        }
    }


    public boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

}