package com.linkmoa.source.domain.member.error;

import com.linkmoa.source.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements ErrorCode {


    MEMBER_NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND,"MEMBER_001","Member email을 찾지 못했습니다");

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;
    MemberErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message =message;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

}
