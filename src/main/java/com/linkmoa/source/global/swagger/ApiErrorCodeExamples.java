/*
package com.linkmoa.source.global.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.linkmoa.source.global.error.code.spec.ErrorCode;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCodeExamples {

	*/
/* 사용 예시

   @ApiErrorCodeEx(SiteErrorCode.class) // enum 클래스 자체를 넘김
	public ResponseEntity<?> someApiMethod() {
		// 해당 메소드에서 사용하고자 하는 에러 코드를 처리
		return ResponseEntity.ok().build();
	}*//*

	Class<? extends ErrorCode> value(); // ErrorCode를 구현한 enum 클래스 타입을 배열로 받음

}
*/
