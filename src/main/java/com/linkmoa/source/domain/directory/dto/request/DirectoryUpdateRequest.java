package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DirectoryUpdateRequest(
	BaseRequest baseRequest,
	@NotNull @Size(max = 45) String directoryName,
	@Size(max = 300) String directoryDescription,
	@NotNull Long directoryId

) {
}
