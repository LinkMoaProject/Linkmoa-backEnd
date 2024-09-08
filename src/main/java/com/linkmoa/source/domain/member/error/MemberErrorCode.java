package com.linkmoa.source.domain.member.error;

import com.linkmoa.source.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND,"Member email을 찾지 못했습니다.");

    private final HttpStatus httpStatus;
    private final String message;


}
