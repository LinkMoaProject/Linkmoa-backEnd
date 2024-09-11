package com.linkmoa.source.domain.site.dto.request;

import jakarta.validation.constraints.NotNull;

public record SiteListGetRequestDto(
        @NotNull Long directoryid
) {
}
