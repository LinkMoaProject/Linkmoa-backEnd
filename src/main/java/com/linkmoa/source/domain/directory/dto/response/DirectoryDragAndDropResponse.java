package com.linkmoa.source.domain.directory.dto.response;

import lombok.Builder;

@Builder
public record DirectoryDragAndDropResponse(
        Long targetId,
        Long targetIdx,
        String targetType
) {
}
