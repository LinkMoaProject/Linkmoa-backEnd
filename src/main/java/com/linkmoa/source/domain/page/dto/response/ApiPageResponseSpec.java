package com.linkmoa.source.domain.page.dto.response;

import org.springframework.http.HttpStatus;

import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiPageResponseSpec<T> extends ApiResponseSpec {

	private T data;

	@Builder
	public ApiPageResponseSpec(HttpStatus httpStatusCode, String successMessage, T data) {
		super(httpStatusCode, successMessage);
		this.data = data;
	}
}
