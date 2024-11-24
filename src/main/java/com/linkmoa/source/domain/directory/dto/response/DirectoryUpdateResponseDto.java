package com.linkmoa.source.domain.directory.dto.response;

import lombok.Builder;

@Builder
public record DirectoryUpdateResponseDto(
        String directoryName,
        Long parentDirectoryId
) {
}
