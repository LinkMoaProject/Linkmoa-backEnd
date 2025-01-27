package com.linkmoa.source.domain.directory.error;

import com.linkmoa.source.global.error.code.spec.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DirectoryErrorCode implements ErrorCode {


    DIRECTORY_NOT_FOUND(HttpStatus.NOT_FOUND,"Directory를 찾을 수 없습니다."),
    REQUEST_ALREADY_ACCEPTED(HttpStatus.BAD_REQUEST, "이미 수락된 요청은 상태를 변경할 수 없습니다."),
    UNSUPPORTED_TARGET_TYPE(HttpStatus.BAD_REQUEST, "지원되지 않는 타입입니다.");


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
