package com.linkmoa.source.domain.dispatch.exception;

import com.linkmoa.source.domain.dispatch.error.DispatchErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DispatchException extends RuntimeException {
	private final DispatchErrorCode errorCode;

}
