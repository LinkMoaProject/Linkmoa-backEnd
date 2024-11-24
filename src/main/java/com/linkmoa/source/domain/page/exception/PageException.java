package com.linkmoa.source.domain.page.exception;


import com.linkmoa.source.domain.page.error.PageErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PageException extends RuntimeException {
    private final PageErrorCode pageErrorCode;
}
