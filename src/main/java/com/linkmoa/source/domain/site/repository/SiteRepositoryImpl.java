package com.linkmoa.source.domain.site.repository;


import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import com.linkmoa.source.domain.site.entity.QSite;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.linkmoa.source.domain.site.entity.QSite.site;
import static com.linkmoa.source.domain.directory.entity.QDirectory.directory;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SiteRepositoryImpl implements SiteRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SiteDetailResponse> findSitesDetails(Long directoryId) {

        List<SiteDetailResponse> siteDetailResponses =jpaQueryFactory
                .selectFrom(site)
                .where(site.directory.id.eq(directoryId))
                .fetch()
                .stream()
                .map(s -> SiteDetailResponse.builder()
                        .siteId(s.getId())
                        .siteUrl(s.getSiteUrl())
                        .siteName(s.getSiteName())
                        .build())
                .collect(Collectors.toList());

        return siteDetailResponses;
    }

    @Override
    public void decrementOrderIndexesAfterSiteDeletion(Directory parentDirectory, Integer deleteOrderIndex) {
        jpaQueryFactory.update(directory)
                .set(directory.orderIndex, directory.orderIndex.subtract(1))
                .where(directory.parentDirectory.eq(parentDirectory)
                        .and(directory.orderIndex.gt(deleteOrderIndex)))
                .execute();

        jpaQueryFactory.update(site)
                .set(site.orderIndex, site.orderIndex.subtract(1))
                .where(site.directory.eq(parentDirectory)
                        .and(site.orderIndex.gt(deleteOrderIndex)))
                .execute();
    }
}
