package com.linkmoa.source.domain.site.repository;

import java.util.List;

import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import com.linkmoa.source.domain.site.dto.response.SiteSimpleResponse;

public interface SiteRepositoryCustom {
	List<SiteDetailResponse> findSitesDetails(Long directoryId, List<Long> favoriteSiteIds);

	List<SiteSimpleResponse> findFavoriteSites(List<Long> favoriteSitesIds);

}
