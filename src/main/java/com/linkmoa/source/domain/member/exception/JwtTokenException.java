package com.linkmoa.source.domain.member.exception;

import com.linkmoa.source.domain.member.error.JwtErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class JwtTokenException extends RuntimeException{
    private final JwtErrorCode jwtErrorCode;
}
