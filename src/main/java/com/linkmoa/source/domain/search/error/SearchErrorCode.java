/*
package com.linkmoa.source.domain.search.error;


import com.linkmoa.source.global.error.code.spec.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SearchErrorCode implements ErrorCode {


    SAVE_DIRECTORY_TO_ES_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Elasticsearch로 Directory 데이터 저장 중 에러가 발생했습니다."),
    SAVE_SITE_TO_ES_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Elasticsearch로 Site 데이터 저장 중 에러가 발생했습니다."),
    ES_SYNC_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Elasticsearch 실행 도중 에러가 발생했습니다."),


    ;




    SearchErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    private HttpStatus httpStatus;
    private String errorMessage;
    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
*/
