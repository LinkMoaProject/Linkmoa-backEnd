package com.linkmoa.source.global.spec;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseSpec {

    private HttpStatus httpStatusCode;
    private String successCode; //SUCESS_000으로 통일 (임시)
    private String successMessage;

    public ApiResponseSpec(HttpStatus httpStatusCode, String successCode, String successMessage) {
        this.httpStatusCode = httpStatusCode;
        this.successCode = successCode;
        this.successMessage = successMessage;
    }
}
