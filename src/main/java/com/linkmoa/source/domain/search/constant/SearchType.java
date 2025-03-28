package com.linkmoa.source.domain.search.constant;

public enum SearchType {
	TITLE("제목 검색"),
	CONTENT("내용 검색"),
	TITLE_CONTENT("제목 + 내용 검색");
	private final String description;

	SearchType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
