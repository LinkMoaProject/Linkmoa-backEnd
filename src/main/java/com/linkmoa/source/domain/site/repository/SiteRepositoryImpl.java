package com.linkmoa.source.domain.site.repository;

import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.linkmoa.source.domain.site.entity.QSite.site;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SiteRepositoryImpl implements SiteRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SiteDetailResponse> findSitesDetails(Long directoryId, Set<Long> favoriteSiteIds) {

        return jpaQueryFactory
                .selectFrom(site)
                .where(site.directory.id.eq(directoryId))
                .fetch()
                .stream()
                .map(s -> SiteDetailResponse.builder()
                        .siteId(s.getId())
                        .siteUrl(s.getSiteUrl())
                        .siteName(s.getSiteName())
                        .isFavorite(favoriteSiteIds.contains(s.getId()))
                        .build())
                .collect(Collectors.toList());

    }

}
