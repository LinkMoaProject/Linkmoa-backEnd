package com.linkmoa.source.domain.site.exception;

import com.linkmoa.source.domain.site.error.SiteErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SiteException extends RuntimeException {
	private final SiteErrorCode siteErrorCode;
}
