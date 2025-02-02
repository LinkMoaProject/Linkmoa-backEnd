package com.linkmoa.source.domain.Favorite.error;


import com.linkmoa.source.global.error.code.spec.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FavoriteErrorCode implements ErrorCode {

    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND,"즐겨찾기 아이템(디렉토리,사이트)을 찾을 수 없습니다."),
    FAVORITE_DELETE_FAILED(HttpStatus.BAD_REQUEST,"즐겨찾기 아이템(디렉토리,사이트)를 삭제 중 오류가 발생했습니다."),
    FAVORITE_CREATE_FAILED(HttpStatus.BAD_REQUEST,"즐겨찾기 아이템(디렉토리,사이트)를 생성 중 중 오류가 발생했습니다.");
    private HttpStatus httpStatus;
    private String errorMessage;

    FavoriteErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
