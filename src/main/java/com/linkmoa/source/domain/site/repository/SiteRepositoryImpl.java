package com.linkmoa.source.domain.site.repository;


import com.linkmoa.source.domain.site.dto.response.SiteMainResponse;
import com.linkmoa.source.domain.site.entity.QSite;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SiteRepositoryImpl implements SiteRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SiteMainResponse> findSitesDetails(Long directoryId) {

        QSite site = QSite.site;
        List<SiteMainResponse> sites =jpaQueryFactory
                .select(site)
                .where(site.directory.id.eq(directoryId))
                .fetch()
                .stream()
                .map(s -> SiteMainResponse.builder()
                        .siteId(s.getId())
                        .siteUrl(s.getSiteUrl())
                        .siteName(s.getSiteName())
                        .build())
                .collect(Collectors.toList());

        return sites;
    }
}
