package com.linkmoa.source.global.exception;

import lombok.Getter;

@Getter
public class CookieNotFoundException extends RuntimeException {

	/**
	 * 생성자는 이 예외가 발생할 때 메시지를 전달받아 부모 클래스인 RuntimeException의 생성자에 전달
	 * super(message)는 RuntimeException 클래스의 생성자를 호출하여 예외 메시지를 초기화함
	 * @param message
	 */
	public CookieNotFoundException(String message) {
		super(message);
	}

}
