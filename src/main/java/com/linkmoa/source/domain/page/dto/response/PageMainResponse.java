package com.linkmoa.source.domain.page.dto.response;

import com.linkmoa.source.domain.directory.dto.response.DirectoryMainResponse;
import com.linkmoa.source.domain.site.dto.response.SiteMainResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record PageMainResponse(

    Long pageId,
    String pageTitle,
    String pageDescription,
    List<DirectoryMainResponse> directoryMainResponses,
    List<SiteMainResponse> siteMainRespons

    ){

}
