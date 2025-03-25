package com.linkmoa.source.domain.site.dto.response;

import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.Builder;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class ApiSiteResponse<T> extends ApiResponseSpec {

	private T data;

	@Builder
	public ApiSiteResponse(HttpStatus httpStatusCode, String successMessage, T data) {
		super(httpStatusCode, successMessage);
		this.data = data;
	}
}
