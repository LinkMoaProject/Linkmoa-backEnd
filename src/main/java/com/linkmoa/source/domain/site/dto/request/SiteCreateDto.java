package com.linkmoa.source.domain.site.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SiteCreateDto {
	public record Request(

		BaseRequest baseRequest,
		@NotBlank @Size(max = 30) String siteName,
		@NotBlank String siteUrl,
		Long directoryId
	) {

	}

}
