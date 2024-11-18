package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DirectoryCreateRequestDto(

        BaseRequestDto baseRequestDto,
        @NotNull @Size(max=20) String directoryName,
        Long parentDirectoryId,
        String directoryDescription
) {
}
