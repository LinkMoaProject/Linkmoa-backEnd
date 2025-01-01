package com.linkmoa.source.domain.directory.dto.response;


import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record DirectoryResponse(
        List<DirectoryDetailResponse> directoryDetailResponses,
        List<SiteDetailResponse> siteDetailResponses,
        String targetDirectoryName,
        String targetDirectoryDescription
) {
}
