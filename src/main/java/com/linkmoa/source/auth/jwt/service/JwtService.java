package com.linkmoa.source.auth.jwt.service;



import com.linkmoa.source.auth.jwt.provider.JwtClaimExtractor;
import com.linkmoa.source.auth.jwt.provider.JwtCookieManager;
import com.linkmoa.source.auth.jwt.provider.JwtTokenProvider;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtClaimExtractor jwtClaimExtractor;
    private final JwtCookieManager jwtCookieManager;
    private final MemberRepository memberRepository;

    public String createAccessToken(String email, String role) {
        return jwtTokenProvider.createAccessToken(email, role);
    }

    public String createRefreshToken() {
        return jwtTokenProvider.createRefreshToken();
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    public String getEmail(String token) {
        return jwtClaimExtractor.getEmail(token);
    }

    public String getRole(String token) {
        return jwtClaimExtractor.getRole(token);
    }

    public String getRefreshTokenFromCookies(Cookie[] cookies) {
        return jwtCookieManager.getRefreshTokenFromCookies(cookies);
    }

    public Cookie createRefreshCookie(String refreshToken) {
        return jwtCookieManager.createCookie("refresh_token", refreshToken, 14 * 24 * 60 * 60);
    }

    public String getAccessTokenFromRefresh(String refreshToken){
        String accessToken = null;

        if (validateToken(refreshToken)) {
            String email = getEmail(refreshToken);
            String role = getRole(refreshToken);
            accessToken = createAccessToken(email, role);
        }

        return accessToken;
    }


}