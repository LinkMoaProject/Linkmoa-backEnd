package com.linkmoa.source.domain.page.dto.response;

import com.linkmoa.source.domain.page.contant.PageType;
import lombok.Builder;

@Builder
public record PagesResponse(
        Long pageId,
        String pageTitle,
        PageType pageType
) {
}
