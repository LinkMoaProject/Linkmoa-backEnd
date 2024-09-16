package com.linkmoa.source.domain.site.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SiteUpdateRequestDto(
        //null 불가, "" 가능, " "가능
        @NotNull @Size(max=30) String siteName,

        //null 불가, "" 불가, " "불가
        @NotBlank String siteUrl,
        @NotNull Long siteId
) {
}
