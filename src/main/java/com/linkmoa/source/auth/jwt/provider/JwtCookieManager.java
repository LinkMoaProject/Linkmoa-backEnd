package com.linkmoa.source.auth.jwt.provider;

import com.linkmoa.source.global.exception.CookieNotFoundException;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieManager {

    public Cookie createCookie(String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    public String getRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new CookieNotFoundException("쿠키가 존재하지 않습니다.");
        }

        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new CookieNotFoundException("리프레시 토큰 쿠키가 없습니다.");
    }
}