package com.linkmoa.source.auth.jwt.error;

import com.linkmoa.source.auth.jwt.dto.response.TokenStatus;
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
