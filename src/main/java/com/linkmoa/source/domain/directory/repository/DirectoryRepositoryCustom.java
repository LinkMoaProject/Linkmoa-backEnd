package com.linkmoa.source.domain.directory.repository;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.entity.Directory;

import java.util.List;

public interface DirectoryRepositoryCustom {
    List<DirectoryDetailResponse> findDirectoryDetails(Long directoryId);
    void decrementOrderIndexesAfterDeletion(Directory parentDirectory, Integer deleteOrderIndex);
}
