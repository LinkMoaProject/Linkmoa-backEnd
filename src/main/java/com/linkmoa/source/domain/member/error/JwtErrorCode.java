package com.linkmoa.source.domain.member.error;

import org.springframework.http.HttpStatus;

import com.linkmoa.source.global.error.code.spec.ErrorCode;

import lombok.Getter;

@Getter
public enum JwtErrorCode implements ErrorCode {

	NO_EXIST_AUTHORIZATION_HEADER_EXCEPTION(HttpStatus.BAD_REQUEST, "Authorization 헤더가 없거나 Bearer로 시작하지 않습니다"),
	EXPIRED_ACCESS_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
		"Access token이 만료되었습니다. Refresh token을 사용하세요."),

	EXPIRED_REFRESH_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
		"Refresh token이 만료되었습니다. 다시 로그인하세요."),

	NOT_VALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
		"유효하지 않은 JWT 토큰입니다."),

	UNSUPPORTED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
		"지원되지 않는 JWT 토큰입니다."),

	MISMATCH_CLAIMS_EXCEPTION(HttpStatus.UNAUTHORIZED,
		"JWT 토큰의 클레임이 일치하지 않거나 토큰이 없습니다."),

	REFRESH_TOKEN_COOKIE_NULL_EXCEPTION(HttpStatus.BAD_REQUEST,
		"쿠키에 담긴 Refresh token이 null 입니다"),

	REFRESH_TOKEN_MISMATCH_EXCEPTION(HttpStatus.UNAUTHORIZED,
		"해당 Refresh token과 DB에 저장된 member의 token이 일치하는게 없습니다. 다시 로그인하세요.");

	private HttpStatus httpStatus;
	private String errorMessage;

	JwtErrorCode(HttpStatus httpStatus, String errorMessage) {
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
