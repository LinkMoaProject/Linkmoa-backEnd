package com.linkmoa.source.domain.directory.dto.request;

import com.linkmoa.source.domain.favorite.constant.ItemType;
import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class DirectoryDragAndDropDto {

	public record Request(
		BaseRequest baseRequest,
		@NotNull Long targetId,
		@NotNull ItemType itemType,
		@NotNull Integer targetOrderIndex,
		@NotNull Long parentDirectoryId
	) {

	}

	@Builder
	public record Response(
		Long targetId,
		Integer targetOrderIndex,
		String itemType
	) {

	}
}