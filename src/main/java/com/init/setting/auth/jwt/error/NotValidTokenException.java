package com.init.setting.auth.jwt.error;

import com.init.setting.auth.jwt.dto.response.TokenStatus;
import lombok.Getter;

@Getter
public class NotValidTokenException extends RuntimeException{
    private JwtErrorCode errorCode;
    private TokenStatus tokenStatus;


    public NotValidTokenException(JwtErrorCode errorCode, TokenStatus tokenStatus) {
        this.errorCode = errorCode;
        this.tokenStatus = tokenStatus;
    }
}
