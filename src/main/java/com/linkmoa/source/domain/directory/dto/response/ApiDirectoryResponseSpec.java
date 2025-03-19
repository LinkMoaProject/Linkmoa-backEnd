package com.linkmoa.source.domain.directory.dto.response;

import org.springframework.http.HttpStatus;

import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiDirectoryResponseSpec<T> extends ApiResponseSpec {

	private T data;

	@Builder
	public ApiDirectoryResponseSpec(HttpStatus httpStatusCode, String successMessage, T data) {
		super(httpStatusCode, successMessage);
		this.data = data;
	}
}
