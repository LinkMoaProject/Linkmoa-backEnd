package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;

public record DirectoryIdRequest(
	BaseRequest baseRequest,
	@NotNull Long directoryId
) {
}
