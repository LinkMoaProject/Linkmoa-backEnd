package com.linkmoa.source.domain.directory.dto.response;


import com.linkmoa.source.domain.search.document.DirectoryDocument;
import lombok.Builder;

import java.util.Set;

@Builder
public record DirectoryDetailResponse(
        Long directoryId,
        String directoryName,
        Integer orderIndex,
        Boolean isFavorite
) {
    public static DirectoryDetailResponse from(DirectoryDocument document, Set<Long> favoriteDirectoryIds) {
        return DirectoryDetailResponse.builder()
                .directoryId(Long.parseLong(document.getId())) // Elasticsearch _id 변환
                .directoryName(document.getTitle())
                .orderIndex(document.getOrderIndex())
                .isFavorite(favoriteDirectoryIds.contains(Long.parseLong(document.getId())))
                .build();
    }

}
