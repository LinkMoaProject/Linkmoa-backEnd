package com.linkmoa.source.domain.page.repository;

import java.util.List;

import com.linkmoa.source.domain.page.dto.response.PageResponse;

public interface PageRepositoryCustom {

	List<PageResponse> findAllPagesByMemberId(Long memberId);

	Long findRootDirectoryIdByPageId(Long pageId);

}
