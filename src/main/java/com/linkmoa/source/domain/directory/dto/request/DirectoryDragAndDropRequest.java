package com.linkmoa.source.domain.directory.dto.request;


import com.linkmoa.source.global.dto.request.BaseRequest;
import lombok.Builder;

@Builder
public record DirectoryDragAndDropRequest(
        BaseRequest baseRequest,
        Long targetId,
        String targetType,
        Long targetIdx,
        Long parentDirectoryId
) {
}
