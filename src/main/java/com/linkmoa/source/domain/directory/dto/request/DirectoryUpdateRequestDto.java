package com.linkmoa.source.domain.directory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DirectoryUpdateRequestDto(
        @NotNull @Size(max=20) String direcotryName,
        Long directoryId
) {
}
