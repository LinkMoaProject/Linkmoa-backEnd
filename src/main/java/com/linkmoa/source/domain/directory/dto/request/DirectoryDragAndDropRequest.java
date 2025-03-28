package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DirectoryDragAndDropRequest(
	BaseRequest baseRequest,
	@NotNull Long targetId,
	@NotNull TargetType targetType,
	@NotNull Integer targetOrderIndex,
	@NotNull Long parentDirectoryId
) {

	public enum TargetType {
		DIRECTORY,
		SITE
	}
}