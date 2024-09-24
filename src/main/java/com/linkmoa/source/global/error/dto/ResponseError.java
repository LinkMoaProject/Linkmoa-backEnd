package com.linkmoa.source.global.error.dto;


import com.linkmoa.source.global.error.code.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseError {

    private HttpStatus httpStatusCode;
    private String errorMessage;
    private String status;


    @Builder
    public ResponseError(HttpStatus httpStatusCode,String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
        this.status="FAIL";
    }
}
