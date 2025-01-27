package com.linkmoa.source.domain.directory.dto.request;


import com.linkmoa.source.global.dto.request.BaseRequest;
import lombok.Builder;

@Builder
public record DirectoryDragAndDropRequest(
        BaseRequest baseRequest,
        Long targetId,
        TargetType targetType,
        Integer targetOrderIndex,
        Long parentDirectoryId
) {

    public enum TargetType{
        DIRECTORY,
        SITE
    }
}