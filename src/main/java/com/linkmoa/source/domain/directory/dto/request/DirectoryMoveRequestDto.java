package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequestDto;

public record DirectoryMoveRequestDto(
        BaseRequestDto baseRequestDto,
        Long sourceDirectoryId, // 원본 디렉토리 ID
        Long targetDirectoryId  // 이동 대상 디렉토리 ID

) {
}
