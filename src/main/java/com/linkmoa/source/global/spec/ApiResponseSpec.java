package com.linkmoa.source.global.spec;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponseSpec<T> {

	private final int status;
	private final String message;
	private final T data;

	@Builder
	private ApiResponseSpec(HttpStatus status, String message, T data) {
		this.status = status.value();
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResponseSpec<T> success(HttpStatus status, String message, T data) {
		return new ApiResponseSpec<>(status, message, data);
	}

	public static <T> ApiResponseSpec<T> success(HttpStatus status, String message) {
		return new ApiResponseSpec<>(status, message, null);
	}

	public static <T> ApiResponseSpec<T> fail(HttpStatus status, String message, T data) {
		return new ApiResponseSpec<>(status, message, data);
	}

	public static <T> ApiResponseSpec<T> fail(HttpStatus status, String message) {
		return new ApiResponseSpec<>(status, message, null);
	}
}
