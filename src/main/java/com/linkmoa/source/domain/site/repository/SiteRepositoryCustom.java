package com.linkmoa.source.domain.site.repository;

import com.linkmoa.source.domain.site.dto.response.SiteMainResponse;

import java.util.List;

public interface SiteRepositoryCustom {
    List<SiteMainResponse> findSitesDetails(Long directoryId);
}
