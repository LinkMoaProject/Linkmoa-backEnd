package com.linkmoa.source.auth.jwt.error;

import com.linkmoa.source.global.error.code.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JwtErrorCode implements ErrorCode {

    //401 JWT 인증 관련 오류
    EXPIRED_ACCESS_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "Access token이 만료되었습니다."),

    EXPIRED_REFRESH_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "Refresh token이 만료되었습니다."),

    NOT_VALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "유효하지 않은 JWT 토큰입니다."),

    UNSUPPORTED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "지원되지 않는 JWT 토큰입니다."),

    MISMATCH_CLAIMS_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "JWT 토큰의 클레임이 일치하지 않거나 토큰이 없습니다."),

    REFRESH_TOKEN_MISMATCH_EXCEPTION(HttpStatus.UNAUTHORIZED,
            "해당 Refresh token과 DB에 저장된 member의 token이 일치하는게 없습니다. 다시 로그인하세요.");



    private HttpStatus httpStatus;
    private String errorMessage;

    JwtErrorCode(HttpStatus httpStatus,  String errorMessage) {
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
