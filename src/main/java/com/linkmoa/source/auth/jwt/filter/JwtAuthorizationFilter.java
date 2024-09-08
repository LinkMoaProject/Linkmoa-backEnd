package com.linkmoa.source.auth.jwt.filter;


import com.linkmoa.source.auth.jwt.service.JwtService;
import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // filter를 거치고 싶지 않은 path를 여기서 관리함
        String[] excludePathLists = {"/favicon.ico", "/swagger-ui/index.html", "/api/jwt/reissue"};
        String[] excludePathStartsWithLists = {"/login", "/oauth2", "/api/members", "/v3", "/swagger-ui", "/ws"};

        String path = request.getRequestURI();

        // 해당 경로로 시작하는 uri에 대해서는 true를 반환하고 filter를 거치지 않음
        boolean startsWithExcludedPath = Arrays.stream(excludePathStartsWithLists).
                anyMatch(path::startsWith);

        // excludePathLists과 같은 uri로 매칭되면 true를 반환하고 filter를 거치지않음.
        boolean matchesExcludedPath = Arrays.stream(excludePathLists)
                .anyMatch((excludePath) -> excludePath.equals(path));

        return startsWithExcludedPath || matchesExcludedPath;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //헤더에서 access 키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access_token");
        //String accesstoken = request.getHeader("Authorization").substring(7);

        log.info("jwt doFilterInternal access token : {} ",accessToken);
        Member member = null;
        if (jwtService.validateToken(accessToken)) {
            //JWT 토큰을 파싱해서 member 정보를 가져옴
            String email = jwtService.getEmail(accessToken);
            member = memberService.findMemberByEmail(email);

        }


        PrincipalDetails principalDetails = new PrincipalDetails(member);

        Authentication authToken = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        // 최종적으로 SecurityContextHolder에 유저의 세션을 등록시킴.
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }

    /**
     * 유저를 authentication 해주는 메소드
     */
    private static void authentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Member member) throws IOException, ServletException {
        // PrincipalDetails에 유저 정보를 담기
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authenticationToken
                = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        // 세션에 사용자 등록, 해당 사용자는 스프링 시큐리티에 의해서 인증됨
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
