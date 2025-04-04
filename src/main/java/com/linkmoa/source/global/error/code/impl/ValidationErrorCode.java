package com.linkmoa.source.global.error.code.impl;

import org.springframework.http.HttpStatus;

import com.linkmoa.source.global.error.code.spec.ErrorCode;

import lombok.Getter;

@Getter
public enum ValidationErrorCode implements ErrorCode {

	UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "해당 명령에 대한 권한이 없습니다."),
	MISSING_BASE_REQUEST_DTO(HttpStatus.BAD_REQUEST, "BaseRequestDto가 요청 DTO에 누락되었습니다."),
	REQUEST_ALREADY_ACCEPTED(HttpStatus.BAD_REQUEST, "이미 수락된 요청입니다."),
	REQUEST_ALREADY_REJECTED(HttpStatus.BAD_REQUEST, "이미 거절된 요청입니다.");

	ValidationErrorCode(HttpStatus httpStatus, String errorMessage) {
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
	}

	private HttpStatus httpStatus;
	private String errorMessage;

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
