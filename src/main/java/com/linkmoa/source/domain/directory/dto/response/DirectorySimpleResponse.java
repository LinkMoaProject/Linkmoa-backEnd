package com.linkmoa.source.domain.directory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@AllArgsConstructor
public class DirectorySimpleResponse {
	private Long directoryId;
	private String directoryName;
	private Boolean isFavorite;

}
