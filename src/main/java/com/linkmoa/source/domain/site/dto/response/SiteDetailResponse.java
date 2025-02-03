package com.linkmoa.source.domain.site.dto.response;


import lombok.Builder;

@Builder
public record SiteDetailResponse(
        Long siteId,
        String siteName,
        String siteUrl,
        Boolean isFavorite
) {
}
