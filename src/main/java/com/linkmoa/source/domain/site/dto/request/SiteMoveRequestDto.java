package com.linkmoa.source.domain.site.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequestDto;
import jakarta.validation.constraints.NotNull;

public record SiteMoveRequestDto(
        BaseRequestDto baseRequestDto,
        @NotNull Long siteId,
        @NotNull Long targetDirectoryId  // 이동 대상 디렉토리 ID
) {
}
