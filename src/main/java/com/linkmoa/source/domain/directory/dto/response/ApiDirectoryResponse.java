package com.linkmoa.source.domain.directory.dto.response;

import com.linkmoa.source.global.spec.ApiResponseSpec;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiDirectoryResponse<T> extends ApiResponseSpec {

    private T data;

    @Builder
    public ApiDirectoryResponse(HttpStatus httpStatusCode, String successMessage, T data) {
        super(httpStatusCode, successMessage);
        this.data = data;
    }
}
