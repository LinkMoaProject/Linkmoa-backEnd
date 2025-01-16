package com.linkmoa.source.domain.notification.error;

import com.linkmoa.source.global.error.code.spec.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NotificationErrorCode implements ErrorCode {

    NOTIFICATION_QUERY_EXECUTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "알람 데이터 조회 시, 오류가 발생했습니다.");

    private HttpStatus httpStatus;
    private String errorMessage;

    NotificationErrorCode(HttpStatus httpStatus, String errorMessage) {
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
