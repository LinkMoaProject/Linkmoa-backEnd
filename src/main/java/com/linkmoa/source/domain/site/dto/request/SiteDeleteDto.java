package com.linkmoa.source.domain.site.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;

public class SiteDeleteDto {
	public record Request(
		BaseRequest baseRequest,
		@NotNull Long siteId
	) {

	}
}
