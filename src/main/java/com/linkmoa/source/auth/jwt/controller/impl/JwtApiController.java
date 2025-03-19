package com.linkmoa.source.auth.jwt.controller.impl;

import com.linkmoa.source.auth.jwt.service.JwtService;
import com.linkmoa.source.auth.jwt.refresh.service.RefreshTokenService;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.repository.MemberRepository;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt")
@Slf4j
public class JwtApiController {

	private final JwtService jwtService;
	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final RefreshTokenService refreshTokenService;

	@GetMapping("/access-token")
	public ResponseEntity<ApiResponseSpec> getAccessToken(
		@CookieValue(value = "refresh_token") String refreshToken) {

		try {
			jwtService.validateToken(refreshToken);

			//email
			String email = refreshTokenService.getEmailByRefreshToken(refreshToken);
			log.info("Extracted Email: {}", email);

			Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL));

			// Access Token 생성
			String accessToken = jwtService.createAccessToken(member.getEmail(), String.valueOf(member.getRole()));
			log.info("Generated Access Token: {}", accessToken);

			//redirect url 얻기
			String redirectUrl = memberService.getRedirectUrlForMember(email);
			log.info("Redirect URL: {}", redirectUrl);

			// 응답 생성
			ApiResponseSpec apiResponseSpec = new ApiResponseSpec(HttpStatus.OK, "Access Token 발급 성공");
			return ResponseEntity.ok()
				.header("Authorization", "Bearer " + accessToken)
				.header("Redirect-Url", redirectUrl) // 리다이렉트 URL 헤더 추가
				.body(apiResponseSpec);

		} catch (Exception e) {
			log.error("Error processing access token request", e);
			ApiResponseSpec errorResponse = new ApiResponseSpec(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 처리 중 오류 발생");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(errorResponse);
		}
	}

}
