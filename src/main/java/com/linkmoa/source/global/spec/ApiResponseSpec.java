package com.linkmoa.source.global.spec;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiResponseSpec {

	private HttpStatus httpStatusCode;
	private String successMessage;
	private String status;

	public ApiResponseSpec(HttpStatus httpStatusCode, String successMessage) {
		this.httpStatusCode = httpStatusCode;
		this.successMessage = successMessage;
		this.status = "SUCCESS";
	}
}
