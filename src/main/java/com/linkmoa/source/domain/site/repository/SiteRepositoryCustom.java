package com.linkmoa.source.domain.site.repository;

import java.util.List;
import java.util.Set;

import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;

public interface SiteRepositoryCustom {
	List<SiteDetailResponse> findSitesDetails(Long directoryId, Set<Long> favoriteSiteIds);

	List<SiteDetailResponse> findFavoriteSites(Set<Long> favoriteSitesIds);

}
