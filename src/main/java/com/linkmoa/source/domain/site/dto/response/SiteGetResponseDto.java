package com.linkmoa.source.domain.site.dto.response;

import lombok.Builder;

@Builder
public record SiteGetResponseDto(
	String siteName,
	String siteUrl
) {
}
