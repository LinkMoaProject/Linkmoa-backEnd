package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;
import jakarta.validation.constraints.NotNull;

public record DirectoryMoveRequest(
        BaseRequest baseRequest,
        @NotNull Long sourceDirectoryId, // 원본 디렉토리 ID
        @NotNull Long targetDirectoryId  // 이동 대상 디렉토리 ID

) {
}
