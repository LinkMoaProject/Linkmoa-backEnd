package com.linkmoa.source.domain.member.exception;


import com.linkmoa.source.domain.member.error.MemberErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberException extends RuntimeException {

    private final MemberErrorCode memberErrorCode;

}
