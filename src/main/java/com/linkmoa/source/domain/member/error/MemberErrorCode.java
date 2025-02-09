package com.linkmoa.source.domain.member.error;

import com.linkmoa.source.global.error.code.spec.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements ErrorCode {


    MEMBER_NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND,"Member email을 찾지 못했습니다"),
    MEMBER_EXIST_EMAIL(HttpStatus.BAD_REQUEST,"이미 존재하는 email입니다.");

    private HttpStatus httpStatus;
    private String errorMessage;
    MemberErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage =errorMessage;
    }
    @Override
    public String getErrorMessage(){
        return errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

}
