package com.linkmoa.source.global.spec;


import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseErrorSpec {

    private HttpStatus httpStatusCode;
    private String errorMessage;
    private String status;


    @Builder
    public ApiResponseErrorSpec(HttpStatus httpStatusCode, String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
        this.status="FAIL";
    }
}
