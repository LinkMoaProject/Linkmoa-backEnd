package com.linkmoa.source.domain.page.exception;

import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.page.error.PageErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

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
