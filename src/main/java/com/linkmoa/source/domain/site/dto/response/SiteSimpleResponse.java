package com.linkmoa.source.domain.site.dto.response;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class SiteSimpleResponse {
	private Long siteId;
	private String siteName;
	private Boolean isFavorite;
}
