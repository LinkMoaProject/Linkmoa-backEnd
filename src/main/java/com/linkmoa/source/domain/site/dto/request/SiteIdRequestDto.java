package com.linkmoa.source.domain.site.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SiteIdRequestDto(
        @NotNull Long siteId
) {
}
