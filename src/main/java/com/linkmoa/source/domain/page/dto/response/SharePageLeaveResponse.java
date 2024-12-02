package com.linkmoa.source.domain.page.dto.response;

import lombok.Builder;

@Builder
public record SharePageLeaveResponse(
        String pageTitle,
        Long pageId
) {
}
