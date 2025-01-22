package com.linkmoa.source.domain.directory.dto.response;


import lombok.Builder;

@Builder
public record DirectoryDetailResponse(
        Long directoryId,
        String directoryName,
        Integer orderIndex
) {

}
