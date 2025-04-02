package com.linkmoa.source.domain.member.exception;

import org.springframework.http.HttpStatus;

import com.linkmoa.source.domain.member.error.MemberErrorCode;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
	private final MemberErrorCode errorCode;

	public MemberException(MemberErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}

	public HttpStatus getHttpStatus() {
		return errorCode.getHttpStatus();
	}

}
