package com.linkmoa.source.domain.Favorite.dto.response;


import com.linkmoa.source.global.spec.ApiResponseSpec;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiFavoriteResponseSpec<T> extends ApiResponseSpec {

    private T data;
    public ApiFavoriteResponseSpec(HttpStatus httpStatusCode, String successMessage) {
        super(httpStatusCode, successMessage);
    }
}
