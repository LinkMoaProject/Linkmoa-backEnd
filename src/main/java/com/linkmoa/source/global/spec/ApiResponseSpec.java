package com.linkmoa.source.global.spec;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseSpec {

    private HttpStatus httpStatusCode;
    private String successMessage;

    public ApiResponseSpec(HttpStatus httpStatusCode, String successMessage) {
        this.httpStatusCode = httpStatusCode;
        this.successMessage = successMessage;
    }
}
