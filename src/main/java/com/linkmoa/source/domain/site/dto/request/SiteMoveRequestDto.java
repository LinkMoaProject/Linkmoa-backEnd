package com.linkmoa.source.domain.site.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;
import jakarta.validation.constraints.NotNull;

public record SiteMoveRequestDto(
        BaseRequest baseRequest,
        @NotNull Long siteId,
        @NotNull Long targetDirectoryId  // 이동 대상 디렉토리 ID
) {
}
