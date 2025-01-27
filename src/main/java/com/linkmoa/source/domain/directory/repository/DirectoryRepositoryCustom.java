package com.linkmoa.source.domain.directory.repository;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.entity.Directory;

import java.util.List;

public interface DirectoryRepositoryCustom {
    List<DirectoryDetailResponse> findDirectoryDetails(Long directoryId);
    void decrementDirectoryOrderIndexes(Directory parentDirectory, Integer orderIndex);
    void decrementSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);
    void decrementDirectoryAndSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);

    void incrementDirectoryOrderIndexes(Directory parentDirectory, Integer orderIndex);
    void incrementSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);
    void incrementDirectoryAndSiteOrderIndexes(Directory parentDirectory, Integer orderIndex);
    void incrementDirectoryOrderIndexesInRange(Directory parentDirectory,Integer startIndex,Integer endIndex,Integer adjustmentValue);
    void incrementSiteOrderIndexesInRange(Directory parentDirectory,Integer startIndex,Integer endIndex,Integer adjustmentValue);
    void incrementDirectoryAndSiteOrderIndexesInRange(Directory parentDirectory,Integer startIndex,Integer endIndex,boolean isIncrement);

}
