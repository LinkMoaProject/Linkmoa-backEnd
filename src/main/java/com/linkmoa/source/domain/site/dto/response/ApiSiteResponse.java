package com.linkmoa.source.domain.site.dto.response;


import com.linkmoa.source.global.spec.ApiResponseSpec;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiSiteResponse<T> extends ApiResponseSpec {

    private T data;
    @Builder
    public ApiSiteResponse(HttpStatus httpStatusCode, String successCode, String successMessage,T data) {
        super(httpStatusCode, successCode, successMessage);
        this.data=data;
    }
}
