package com.linkmoa.source.domain.directory.dto.request;

import jakarta.validation.constraints.NotNull;

public record DirectoryIdRequestDto(
        @NotNull Long directoryId
) {
}
