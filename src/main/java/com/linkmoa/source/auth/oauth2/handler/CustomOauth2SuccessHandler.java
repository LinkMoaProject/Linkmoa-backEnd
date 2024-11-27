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


    /**
     *
     *  CustomOAuth2UserService의 loadUser 메서드에서 Principal 객체를 반환하면,
     *  이 객체가 Authentication 객체에 저장됨.
     *  더 자세히 설명하면, OAuth2 로그인 과정에서 loadUser 메서드가 반환하는 객체는 Authentication 객체의 Principal로 설정됨.
     *
     */

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


        // 테스트용으로 추가한 부분
        String accessToken =jwtService.createAccessToken(email,role);
        response.addCookie(jwtCookieManager.createCookie("refresh_token", refreshToken, 14 * 24 * 60 * 60));
        response.setHeader("Authorization","Bearer "+accessToken);

        log.info("OAuth2 로그인에 성공 하였습니다. access Token : {}",accessToken);
        log.info("OAuth2 로그인에 성공하였습니다. 이메일 : {}",  oauth2User.getEmail());
        log.info("OAuth2 로그인에 성공하였습니다. Refresh Token : {}",  refreshToken);

        /**
         * 자체 회원가입 로직
         * 1.프론트 OAuth2 소셜 로그인 후 쿠키에 리프레쉬 토큰만 응답
         * 2-1. 새로운 유저
         *  - 서버에서 redirect /reissue
         *  - 클라이언트에서 api(엑세스 토큰 요청) 호출
         *  - 응답으로 헤더에 엑세스 토큰, 쿠키에 리프레쉬 토큰 - status 200, 201나 다른 필드로 구분하는 등 
         *  - 클라이언트에서 사인업 페이지로 리다이렉트
         *
         * 2-2. 기존 유저
         *  - 서버에서 redirect /reissue
         *  - 클라이언트에서 api(엑세스 토큰 요청) 호출
         *  - 응답으로 헤더에 엑세스 토큰, 쿠키에 리프레쉬 토큰
         *  - 클라이언트에서 메인 페이지로 리다이렉
         */


        /**
         * ! 정리해서 다시 보여드리기 (우선은 구글만 진행하기)
         * 1.프론트에서 "하이퍼링크"로 소셜 로그인 요청 (소셜 로그인 리다이렉트)
         *
         * 2.백엔드에서 Oauth code(인가코드) 등 로그인 처리를 전부 진행해서 사용자의 정보를 받아옴 ( 이메일 ...)
         *
         * 3.백엔드에서 해당 이메일을 바탕으로 "리프레시 토큰을 생성해서 쿠키에 담아서" 프론트에 응답으로 보내줌
         * => 쿠키 : 리프레시 토큰 , 헤더: 엑세스 토큰을 담아서 보내면 안되냐?
         * => 프론트에서 "하이퍼링크"로 접속을 하면 헤더에 접근을 할 수가 없음
         * => 보통 백엔드에서 쿠키를 사용할 떄, http only (보안)때문에 프론트에서도 쿠키에 접근을 할수가없음.
         *
         * => "쿠키에 바로 접근이 안됨" 응답받은 그대로 다시 요청을 보내시면 됩니다.
         *
         * 4.프론트에서 "쿠키에 있는 리프레시 토큰을 그대로 담아서" 다시 백엔드로 엑세스 토큰 요청(http 요청)을 보냄
         *
         * 5.백엔드에서 "쿠키의 리프레시 토큰"을 바탕으로 엑세스 토큰 생성
         *
         * 6.백엔드에서 쿠키 : 리프레시 토큰 , 헤더에 엑세스 토큰을 담아서 response를 보냄
         *
         * 7.프론트에서 추후 http 요청을 보낼때, 헤더에는 엑세스 토큰을 담고, 쿠키에는 리프레시 토큰을 담아서 요청을 보냄

         */




    }
}
