package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequestDto;
import jakarta.validation.constraints.NotNull;

public record DirectoryMoveRequestDto(
        BaseRequestDto baseRequestDto,
        @NotNull Long sourceDirectoryId, // 원본 디렉토리 ID
        @NotNull Long targetDirectoryId  // 이동 대상 디렉토리 ID

) {
}
