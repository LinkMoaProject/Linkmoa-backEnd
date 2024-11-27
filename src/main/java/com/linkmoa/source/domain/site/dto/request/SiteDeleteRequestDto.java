package com.linkmoa.source.domain.site.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequestDto;
import jakarta.validation.constraints.NotNull;

public record SiteDeleteRequestDto(
        BaseRequestDto baseRequestDto,
        @NotNull Long siteId
) {
}
