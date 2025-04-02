package com.linkmoa.source.global.exception;

import com.linkmoa.source.global.error.code.impl.ValidationErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {
	private final ValidationErrorCode errorCode;
}
