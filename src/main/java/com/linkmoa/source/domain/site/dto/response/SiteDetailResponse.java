package com.linkmoa.source.domain.site.dto.response;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class SiteDetailResponse extends SiteSimpleResponse {
	private Integer orderIndex;
}
