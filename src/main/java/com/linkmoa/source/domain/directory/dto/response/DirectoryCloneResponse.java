package com.linkmoa.source.domain.directory.dto.response;

import lombok.Builder;

@Builder
public record DirectoryCloneResponse(
	Long directoryId,
	String directoryName
) {
}
