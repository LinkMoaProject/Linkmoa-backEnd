package com.linkmoa.source.domain.directory.dto.response;

import java.util.List;

import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;

import lombok.Builder;

@Builder
public record DirectoryResponse(
	List<DirectoryDetailResponse> directoryDetailResponses,
	List<SiteDetailResponse> siteDetailResponses,
	String targetDirectoryName,
	String targetDirectoryDescription
) {
}
