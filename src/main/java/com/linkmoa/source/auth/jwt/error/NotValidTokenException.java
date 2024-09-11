package com.linkmoa.source.auth.jwt.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotValidTokenException extends RuntimeException{
    private final JwtErrorCode errorCode;

}

