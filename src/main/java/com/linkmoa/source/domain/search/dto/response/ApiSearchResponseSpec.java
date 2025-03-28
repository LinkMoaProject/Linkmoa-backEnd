package com.linkmoa.source.domain.search.dto.response;

import org.springframework.http.HttpStatus;

import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiSearchResponseSpec<T> extends ApiResponseSpec {
	private T data;

	@Builder
	public ApiSearchResponseSpec(HttpStatus httpStatusCode, String successMessage, T data) {
		super(httpStatusCode, successMessage);
		this.data = data;
	}
}
