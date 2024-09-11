package com.linkmoa.source.domain.site.error;

import com.linkmoa.source.global.error.code.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SiteErrorCode implements ErrorCode {

    SITE_NOT_FOUND(HttpStatus.NOT_FOUND,"SITE_001","Site를 찾을 수 없습니다.");

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;

    SiteErrorCode(HttpStatus httpStatus, String errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
