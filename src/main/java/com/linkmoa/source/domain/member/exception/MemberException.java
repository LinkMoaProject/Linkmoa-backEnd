package com.linkmoa.source.domain.member.exception;

import com.linkmoa.source.domain.member.error.MemberErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends RuntimeException {
	private final MemberErrorCode memberErrorCode;

	public MemberException(MemberErrorCode memberErrorCode) {
		super(memberErrorCode.getErrorMessage());
		this.memberErrorCode = memberErrorCode;
	}

	public HttpStatus getHttpStatus() {
		return memberErrorCode.getHttpStatus();
	}

}
