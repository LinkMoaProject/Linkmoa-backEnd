package com.linkmoa.source.domain.directory.repository;

import com.linkmoa.source.domain.Favorite.entity.Favorite;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.entity.Directory;

import java.util.List;
import java.util.Set;

public interface DirectoryRepositoryCustom {
    List<DirectoryDetailResponse> findDirectoryDetails(Long directoryId, Set<Long> favoriteDirectoryIds);
    void decrementDirectoryOrderIndexes(Directory parentDirectory, Integer orderIndex);
    void decrementSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);
    void decrementDirectoryAndSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);

    void incrementDirectoryOrderIndexes(Directory parentDirectory, Integer orderIndex);
    void incrementSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);
    void incrementDirectoryAndSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);
    void updateDirectoryOrderIndexesInRange(Directory parentDirectory,Integer startIndex,Integer endIndex,Integer adjustmentValue);
    void updateSiteOrderIndexesInRange(Directory parentDirectory,Integer startIndex,Integer endIndex,Integer adjustmentValue);
    void updateDirectoryAndSiteOrderIndexesInRange(Directory parentDirectory,Integer startIndex,Integer endIndex,boolean isIncrement);

}
