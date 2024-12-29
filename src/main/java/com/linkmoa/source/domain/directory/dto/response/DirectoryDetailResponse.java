package com.linkmoa.source.domain.directory.dto.response;


import com.linkmoa.source.domain.site.dto.response.SiteMainResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record DirectoryDetailResponse(
        List<DirectoryMainResponse> directories,
        List<SiteMainResponse> sites
) {
}
