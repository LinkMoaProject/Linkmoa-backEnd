package com.linkmoa.source.domain.favorite.dto.response;

import java.util.List;

import com.linkmoa.source.domain.directory.dto.response.DirectorySimpleResponse;
import com.linkmoa.source.domain.site.dto.response.SiteSimpleResponse;

import lombok.Builder;

@Builder
public record FavoriteDetailResponse(

	String email,
	List<DirectorySimpleResponse> directorySimpleResponses,
	List<SiteSimpleResponse> siteSimpleResponses
) {
}
