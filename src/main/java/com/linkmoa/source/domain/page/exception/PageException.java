package com.linkmoa.source.domain.page.exception;

import org.springframework.http.HttpStatus;

import com.linkmoa.source.domain.page.error.PageErrorCode;

import lombok.Getter;

@Getter
public class PageException extends RuntimeException {
	private final PageErrorCode pageErrorCode;

	public PageException(PageErrorCode pageErrorCode) {
		super(pageErrorCode.getErrorMessage());
		this.pageErrorCode = pageErrorCode;
	}

	public HttpStatus getHttpStatus() {
		return pageErrorCode.getHttpStatus();
	}

}
