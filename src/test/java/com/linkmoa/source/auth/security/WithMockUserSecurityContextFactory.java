package com.linkmoa.source.auth.security;


import com.linkmoa.source.auth.annotation.WithMockCustomUser;
import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.constant.Role;
import com.linkmoa.source.domain.member.entity.Member;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private SecretKey secretKey;
    private String secret="jwt_seceret=eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTcxMTM2MDk1MywiaWF0IjoxNzExMzYwOTUzfQ.DZzz_c2IrVkUDrhK1rRLiHRZTCMG7fav1Nz7tZZ4RU0";
    private final Long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60;

    private static String accessToken; // 토큰 저장용

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser mockUser) {

        // 비어 있는 SecurityContext 생성
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        // Mock 사용자 정보 생성 (Member 객체)
        Member member = Member.builder()
                .email(mockUser.userEmail())
                .role(Role.valueOf(mockUser.userRole()))
                .provider("mock-provider")
                .providerId("mock-provider-id")
                .build();

        // 명시적으로 ID를 설정
        ReflectionTestUtils.setField(member, "id", 1L); // 1L은 테스트용 ID

        // PrincipalDetails 생성
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        // JWT 생성
        accessToken = createAccessToken(member.getEmail(), member.getRole().name());

        // Authentication 객체 생성 및 SecurityContext에 설정
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                principalDetails,
                null, // 자격 증명은 null
                principalDetails.getAuthorities()
        );
        securityContext.setAuthentication(authToken);

        // RequestContextHolder 초기화 (테스트용 RequestContext 설정)
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("access_token",accessToken);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        return securityContext;
    }

    public String createAccessToken(String email, String role) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                    Jwts.SIG.HS256.key().build().getAlgorithm());

        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .claim("token_type", "access_token")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    public static String getAccessToken(){
        return accessToken;
    }
}
