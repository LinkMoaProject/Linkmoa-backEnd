package com.linkmoa.source.domain.directory.repository;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryMainResponse;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.entity.QDirectory;
import com.linkmoa.source.domain.site.dto.response.SiteMainResponse;
import com.linkmoa.source.domain.site.entity.QSite;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class DirectoryRepositoryImpl implements DirectoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public DirectoryDetailResponse findDirectoryDetails(Long directoryId) {

        QDirectory directory = QDirectory.directory;
        QSite site = QSite.site;


        Directory parentDirectory =jpaQueryFactory
                .selectFrom(directory)
                .where(directory.id.eq(directoryId))
                .fetchOne();


        List<DirectoryMainResponse> directories = jpaQueryFactory
                .selectFrom(directory)
                .where(directory.parentDirectory.id.eq(directoryId))
                .fetch()
                .stream()
                .map(d -> DirectoryMainResponse.builder()
                        .directoryId(d.getId())
                        .directoryName(d.getDirectoryName())
                        .build())
                .collect(Collectors.toList());

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

        return DirectoryDetailResponse.builder()
                .directories(directories)
                .sites(sites)
                .parentDirectoryName(parentDirectory.getDirectoryName())
                .parentDirectoryDescription(parentDirectory.getDirectoryDescription())
                .build();
    }
}
