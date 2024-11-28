package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;



public record DirectoryCreateRequestDto(

        BaseRequestDto baseRequestDto,
        @NotBlank @Size(max=20) String directoryName,
        @NotNull Long parentDirectoryId,
        @Size(max=100) String directoryDescription
) {
}
