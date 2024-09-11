package com.linkmoa.source.global.error.dto;


import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseError {

    private HttpStatus httpStatusCode;
    private String errorCode;
    private String errorMessage;
    @Builder
    public ResponseError(HttpStatus httpStatusCode,String errorCode, String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
