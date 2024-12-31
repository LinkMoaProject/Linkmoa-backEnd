package com.linkmoa.source.domain.site.repository;

import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;

import java.util.List;

public interface SiteRepositoryCustom {
    List<SiteDetailResponse> findSitesDetails(Long directoryId);
}
