package com.linkmoa.source.domain.site.dto.response;


import lombok.Builder;

@Builder
public record SiteResponse(
        Long siteId,
        String siteName,
        String siteUrl
) {
}
