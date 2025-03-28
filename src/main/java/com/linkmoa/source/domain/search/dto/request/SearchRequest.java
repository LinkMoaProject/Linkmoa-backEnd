package com.linkmoa.source.domain.search.dto.request;

import com.linkmoa.source.domain.search.constant.SearchType;

public record SearchRequest(
	Long pageId,
	String keyword,
	SearchType searchType

) {
}
