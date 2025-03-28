package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;

public record DirectoryPasteRequest(

	BaseRequest baseRequest,
	@NotNull Long originalDirectoryId,
	@NotNull Long destinationDirectoryId
) {
}
