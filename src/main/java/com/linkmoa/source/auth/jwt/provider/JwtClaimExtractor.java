package com.linkmoa.source.auth.jwt.provider;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtClaimExtractor {

    private final JwtTokenProvider jwtTokenProvider;
    //private final SecretKey secretKey;

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(jwtTokenProvider.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(jwtTokenProvider.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public boolean isRefreshToken(String token) {
        return "refresh_token".equals(Jwts.parser()
                .verifyWith(jwtTokenProvider.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("token_type"));
    }
}