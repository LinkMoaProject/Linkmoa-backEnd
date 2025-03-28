package com.linkmoa.source.domain.site.error;

import org.springframework.http.HttpStatus;

import com.linkmoa.source.global.error.code.spec.ErrorCode;

import lombok.Getter;

@Getter
public enum SiteErrorCode implements ErrorCode {

	SITE_NOT_FOUND(HttpStatus.NOT_FOUND, "Site를 찾을 수 없습니다."),
	SITE_ERRORCODE_TEST(HttpStatus.NOT_FOUND, "site error code 테스트");

	private HttpStatus httpStatus;
	private String errorMessage;

	SiteErrorCode(HttpStatus httpStatus, String errorMessage) {
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
