package com.linkmoa.source.domain.site.repository;

import java.util.List;

import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;

public interface SiteRepositoryCustom {
	List<SiteDetailResponse> findSitesDetails(Long directoryId, List<Long> favoriteSiteIds);

	List<SiteDetailResponse> findFavoriteSites(List<Long> favoriteSitesIds);

}
