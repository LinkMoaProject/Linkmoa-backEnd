package com.linkmoa.source.domain.member.error;

import com.linkmoa.source.global.error.code.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements ErrorCode {


    MEMBER_NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND,"MEMBER_001","Member email을 찾지 못했습니다");

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;
    MemberErrorCode(HttpStatus httpStatus, String errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage =errorMessage;
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
