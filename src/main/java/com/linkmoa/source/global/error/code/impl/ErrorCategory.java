package com.linkmoa.source.global.error.code.impl;

public enum ErrorCategory {
	MEMBER("회원 처리 중 오류가 발생했습니다."),
	SITE("사이트 정보 처리 중 문제가 발생했습니다."),
	VALIDATION("입력값이 올바르지 않습니다."),
	PAGE("페이지 처리 중 오류가 발생했습니다."),
	DISPATCH("디스패치 요청 처리 중 오류가 발생했습니다."),
	BUSINESS("처리할 수 없는 요청입니다."),
	SYSTEM("예상치 못한 시스템 오류가 발생했습니다."),
	ILLEGAL_ARGUMENT("요청 값이 잘못되었습니다.");
	private final String message;

	ErrorCategory(String message) {
		this.message = message;
	}

	public String message() {
		return message;
	}
}
