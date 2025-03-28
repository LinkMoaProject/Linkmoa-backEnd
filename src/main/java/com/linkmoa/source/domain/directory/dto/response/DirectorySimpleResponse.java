package com.linkmoa.source.domain.directory.dto.response;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class DirectorySimpleResponse {
	private Long directoryId;
	private String directoryName;
	private Boolean isFavorite;
}
