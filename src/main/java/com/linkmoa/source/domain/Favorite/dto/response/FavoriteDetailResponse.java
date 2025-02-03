package com.linkmoa.source.domain.Favorite.dto.response;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record FavoriteDetailResponse(

        String email,
        List<DirectoryDetailResponse> directoryDetailResponses,
        List<SiteDetailResponse> siteDetailResponses
) {
}
