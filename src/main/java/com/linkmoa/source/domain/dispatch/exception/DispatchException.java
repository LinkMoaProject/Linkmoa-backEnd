package com.linkmoa.source.domain.dispatch.exception;


import com.linkmoa.source.domain.dispatch.error.DispatchErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DispatchException extends RuntimeException {
    private final DispatchErrorCode errorCode;

    public DispatchException(DispatchErrorCode errorCode) {
        super(errorCode.getErrorMessage()); // 메시지를 RuntimeException에 전달
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
