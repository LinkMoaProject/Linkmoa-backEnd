package com.linkmoa.source.domain.directory.dto.request;

import java.util.List;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class DirectoryIdDto {

	public record Request(
		BaseRequest baseRequest,
		@NotNull Long directoryId
	) {

	}

	@Builder
	public record Response(
		List<DirectoryDetailResponse> directoryDetailResponses,
		List<SiteDetailResponse> siteDetailResponses,
		String targetDirectoryName,
		String targetDirectoryDescription
	) {

	}

}
