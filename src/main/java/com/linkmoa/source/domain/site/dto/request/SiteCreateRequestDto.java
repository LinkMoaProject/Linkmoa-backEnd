package com.linkmoa.source.domain.site.dto.request;

import com.linkmoa.source.domain.site.entity.Site;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SiteCreateRequestDto(
        @NotBlank @Size(max=30) String siteName,
        @NotBlank String siteUrl,
        Long directoryId
) {


}
