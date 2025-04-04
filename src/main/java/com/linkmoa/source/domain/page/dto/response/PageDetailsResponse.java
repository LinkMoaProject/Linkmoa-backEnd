package com.linkmoa.source.domain.page.dto.response;

import java.util.List;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;

import lombok.Builder;

@Builder
public record PageDetailsResponse(
	Long pageId,
	String pageTitle,
	String pageDescription,
	List<DirectoryDetailResponse> directoryDetailRespons,
	List<SiteDetailResponse> siteDetailResponses

) {

}
