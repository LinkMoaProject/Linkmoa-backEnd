package com.linkmoa.source.domain.site.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@AllArgsConstructor
public class SiteSimpleResponse {
	private Long siteId;
	private String siteName;
	private String siteUrl;
	private Boolean isFavorite;

}
