package com.linkmoa.source.domain.page.error;

import com.linkmoa.source.global.error.code.spec.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PageErrorCode implements ErrorCode {


    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"Page를 찾을 수 없습니다.");

    private HttpStatus httpStatus;
    private String errorMessage;

    PageErrorCode(HttpStatus httpStatus, String errorMessage) {
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
