package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequestDto;
import jakarta.validation.constraints.NotNull;

public record DirectoryDeleteRequestDto(
        BaseRequestDto baseRequestDto,
        @NotNull Long directoryId
) {
}
