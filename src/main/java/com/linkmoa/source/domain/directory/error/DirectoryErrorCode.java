package com.linkmoa.source.domain.directory.error;

import com.linkmoa.source.global.error.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum DirectoryErrorCode implements ErrorCode {


    DIRECTORY_NOT_FOUND(HttpStatus.NOT_FOUND,"Directory를 찾을 수 없습니다.");


    private HttpStatus httpStatus;
    private String errorMessage;


    DirectoryErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage(){
        return errorMessage;
    }
}
