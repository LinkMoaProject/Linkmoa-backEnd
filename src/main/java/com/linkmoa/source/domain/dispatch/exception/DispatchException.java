package com.linkmoa.source.domain.dispatch.exception;

import org.springframework.http.HttpStatus;

import com.linkmoa.source.domain.dispatch.error.DispatchErrorCode;

import lombok.Getter;

@Getter
public class DispatchException extends RuntimeException {
	private final DispatchErrorCode errorCode;

	public DispatchException(DispatchErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}

	public HttpStatus getHttpStatus() {
		return errorCode.getHttpStatus();
	}
}
