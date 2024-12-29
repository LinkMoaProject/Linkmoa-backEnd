package com.linkmoa.source.domain.directory.repository;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryMainResponse;

import java.util.List;

public interface DirectoryRepositoryCustom {
    List<DirectoryMainResponse> findDirectoryDetails(Long directoryId);
}
