package com.linkmoa.source.domain.site.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SiteUpdateRequestDto(

        BaseRequest baseRequest,
        @NotBlank @Size(max=30) String siteName,
        @NotBlank String siteUrl,
        @NotNull Long siteId
) {
}
