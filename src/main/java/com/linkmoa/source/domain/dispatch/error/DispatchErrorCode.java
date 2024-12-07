package com.linkmoa.source.domain.dispatch.error;


import com.linkmoa.source.global.error.code.spec.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DispatchErrorCode implements ErrorCode {

    REQUESR_ALREADY_PROCESSED(HttpStatus.NOT_FOUND , "이미 처리된 요청입니다.");

    private HttpStatus httpStatus;
    private String errorMessage;

    DispatchErrorCode(HttpStatus httpStatus, String errorMessage) {
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
