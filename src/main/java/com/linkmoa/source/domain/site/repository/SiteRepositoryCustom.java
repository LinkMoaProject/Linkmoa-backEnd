package com.linkmoa.source.domain.site.repository;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;

import java.util.List;
import java.util.Set;

public interface SiteRepositoryCustom {
    List<SiteDetailResponse> findSitesDetails(Long directoryId, Set<Long> favoriteSiteIds);
    List<SiteDetailResponse> findFavoriteSites(Set<Long> favoriteSitesIds);

}
