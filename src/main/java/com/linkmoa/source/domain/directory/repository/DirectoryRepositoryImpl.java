package com.linkmoa.source.domain.directory.repository;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.entity.QDirectory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class DirectoryRepositoryImpl implements DirectoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<DirectoryDetailResponse> findDirectoryDetails(Long directoryId) {

        QDirectory directory = QDirectory.directory;

        List<DirectoryDetailResponse> directoryDetailResponses = jpaQueryFactory
                .selectFrom(directory)
                .where(directory.parentDirectory.id.eq(directoryId))
                .orderBy(directory.orderIndex.asc())
                .fetch()
                .stream()
                .map(d -> DirectoryDetailResponse.builder()
                        .directoryId(d.getId())
                        .directoryName(d.getDirectoryName())
                        .orderIndex(d.getOrderIndex())
                        .build())
                .collect(Collectors.toList());

        return directoryDetailResponses;

    }
}
