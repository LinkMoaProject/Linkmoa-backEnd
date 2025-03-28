package com.linkmoa.source.domain.favorite.dto.response;

import java.util.List;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;

import lombok.Builder;

@Builder
public record FavoriteDetailResponse(

	String email,
	List<DirectoryDetailResponse> directoryDetailResponses,
	List<SiteDetailResponse> siteDetailResponses
) {
}
