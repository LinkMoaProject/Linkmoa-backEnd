package com.linkmoa.source.auth.oauth2.handler;

import com.linkmoa.source.auth.jwt.provider.JwtCookieManager;
import com.linkmoa.source.auth.jwt.service.RefreshTokenService;
import com.linkmoa.source.auth.jwt.service.JwtService;
import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    //SimpleUrlAuthenticationSuccessHandler는 인증 성공 후 처리를 담당하는 핸들러


    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final JwtCookieManager jwtCookieManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails oauth2User = (PrincipalDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = oauth2User.getAuthorities();

        String email = oauth2User.getEmail();
        String role = authorities.iterator().next().getAuthority();

        String refreshToken =  jwtService.createRefreshToken();
        refreshTokenService.saveRefreshToken(email,refreshToken);
        response.addCookie(jwtService.createRefreshCookie(refreshToken));

        response.sendRedirect("http://localhost:3000/reissue");
        //response.sendRedirect("https://linkmoa-front.vercel.app/reissue");

        // 테스트용으로 추가한 부분
        String accessToken =jwtService.createAccessToken(email,role);
        response.addCookie(jwtCookieManager.createCookie("refresh_token", refreshToken, 14 * 24 * 60 * 60));
        response.setHeader("Authorization","Bearer "+accessToken);

        log.info("OAuth2 로그인에 성공 하였습니다. access Token : {}",accessToken);
        log.info("OAuth2 로그인에 성공하였습니다. 이메일 : {}",  oauth2User.getEmail());
        log.info("OAuth2 로그인에 성공하였습니다. Refresh Token : {}",  refreshToken);


    }
}
