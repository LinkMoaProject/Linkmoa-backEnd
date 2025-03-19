package com.linkmoa.source.domain.site.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record SiteDetailResponse(
	Long siteId,
	String siteName,
	String siteUrl,
	Integer orderIndex,
	Boolean isFavorite
) {
   /* public static SiteDetailResponse from(SiteDocument document, Set<Long> favoriteSiteIds) {
        return SiteDetailResponse.builder()
                .siteId(Long.parseLong(document.getId())) // Elasticsearch _id 변환
                .siteName(document.getTitle())
                .siteUrl(document.getUrl())
                .orderIndex(document.getOrderIndex())
                .isFavorite(favoriteSiteIds.contains(Long.parseLong(document.getId())))
                .build();
    }*/
}
