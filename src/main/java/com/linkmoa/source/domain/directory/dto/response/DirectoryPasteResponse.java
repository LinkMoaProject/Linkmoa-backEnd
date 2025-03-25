package com.linkmoa.source.domain.directory.dto.response;

import lombok.Builder;

@Builder
public record DirectoryPasteResponse(
	Long pastedirectoryId,
	Long destinationDirectoryId,
	String clonedDirectoryName
) {
}
