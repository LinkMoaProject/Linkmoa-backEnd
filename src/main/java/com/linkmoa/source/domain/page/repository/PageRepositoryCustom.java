package com.linkmoa.source.domain.page.repository;

import com.linkmoa.source.domain.page.dto.response.PageResponse;

import java.util.List;

public interface PageRepositoryCustom {

    List<PageResponse> findAllPagesByMemberId(Long memberId);

}
