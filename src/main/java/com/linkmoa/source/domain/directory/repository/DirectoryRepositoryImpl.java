package com.linkmoa.source.domain.directory.repository;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.linkmoa.source.domain.directory.entity.QDirectory.directory;
import static com.linkmoa.source.domain.site.entity.QSite.site;


import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
public class DirectoryRepositoryImpl implements DirectoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<DirectoryDetailResponse> findDirectoryDetails(Long directoryId) {

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

    @Override
    public void decrementDirectoryOrderIndexes(Directory parentDirectory, Integer orderIndex) {
        jpaQueryFactory.update(directory)
                .set(directory.orderIndex, directory.orderIndex.subtract(1))
                .where(directory.parentDirectory.eq(parentDirectory)
                        .and(directory.orderIndex.gt(orderIndex)))
                .execute();
    }

    @Override
    public void decrementSiteOrderIndexes(Directory parentDirectory, Integer orderIndex) {
        jpaQueryFactory.update(site)
                .set(site.orderIndex, site.orderIndex.subtract(1))
                .where(site.directory.eq(parentDirectory)
                        .and(site.orderIndex.gt(orderIndex)))
                .execute();
    }

    @Override
    public void decrementDirectoryAndSiteOrderIndexes(Directory parentDirectory, Integer orderIndex) {
        decrementDirectoryOrderIndexes(parentDirectory,orderIndex);
        decrementSiteOrderIndexes(parentDirectory,orderIndex);
    }

    @Override
    public void incrementDirectoryOrderIndexes(Directory parentDirectory, Integer orderIndex) {
        jpaQueryFactory.update(directory)
                .set(directory.orderIndex,directory.orderIndex.add(1))
                .where(directory.parentDirectory.eq(parentDirectory)
                        .and(directory.orderIndex.gt(orderIndex)))
                .execute();
    }

    @Override
    public void incrementSiteOrderIndexes(Directory parentDirectory, Integer orderIndex) {
        jpaQueryFactory.update(site)
                .set(site.orderIndex, site.orderIndex.add(1))
                .where(site.directory.eq(parentDirectory)
                        .and(site.orderIndex.gt(orderIndex)))
                .execute();
    }
    @Override
    public void incrementDirectoryAndSiteOrderIndexes(Directory parentDirectory, Integer orderIndex) {
        incrementDirectoryOrderIndexes(parentDirectory, orderIndex);
        incrementSiteOrderIndexes(parentDirectory, orderIndex);
    }

    @Override
    public void updateDirectoryOrderIndexesInRange(Directory parentDirectory, Integer startIndex, Integer endIndex,Integer adjustmentValue) {
        jpaQueryFactory.update(directory)
                .set(directory.orderIndex,directory.orderIndex.add(adjustmentValue))
                .where(directory.parentDirectory.eq(parentDirectory)
                        .and(directory.orderIndex.between(startIndex,endIndex)))
                .execute();
    }

    @Override
    public void updateSiteOrderIndexesInRange(Directory parentDirectory, Integer startIndex, Integer endIndex,Integer adjustmentValue) {
        jpaQueryFactory.update(site)
                .set(site.orderIndex,site.orderIndex.add(adjustmentValue))
                .where(site.directory.eq(parentDirectory)
                        .and(site.orderIndex.between(startIndex,endIndex)))
                .execute();
    }

    @Override
    public void updateDirectoryAndSiteOrderIndexesInRange(Directory parentDirectory, Integer startIndex, Integer endIndex,boolean isIncrement) {
        Integer adjustmentValue = isIncrement ? 1 : -1;

        updateDirectoryOrderIndexesInRange(parentDirectory, startIndex, endIndex,adjustmentValue);
        updateSiteOrderIndexesInRange(parentDirectory,startIndex,endIndex,adjustmentValue);
    }


}
