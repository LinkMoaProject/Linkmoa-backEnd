package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class DirectoryPasteDto {

	public record Request(
		BaseRequest baseRequest,
		@NotNull Long originalDirectoryId,
		@NotNull Long destinationDirectoryId
	) {

	}

	@Builder
	public record Response(
		Long pastedirectoryId,
		Long destinationDirectoryId,
		String clonedDirectoryName
	) {

	}
}
