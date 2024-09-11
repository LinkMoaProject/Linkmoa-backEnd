package com.linkmoa.source.global.error;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseError {

    private HttpStatus httpStatusCode;
    private String errorCode;
    private String errorMessage;

    public ResponseError(ErrorCode errorCode,String errorMessage) {
        this.httpStatusCode = errorCode.getHttpStatus();
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorMessage;
    }
}
