package com.linkmoa.source.domain.page.repository;

import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.dto.response.PagesResponse;

import java.util.List;

public interface PageRepositoryCustom {

    List<PagesResponse> findAllPagesByMemberId(Long memberId);

}
