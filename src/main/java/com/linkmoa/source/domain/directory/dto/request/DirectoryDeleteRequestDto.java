package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequestDto;

public record DirectoryDeleteRequestDto(
        BaseRequestDto baseRequestDto,
        Long directoryId
) {
}
