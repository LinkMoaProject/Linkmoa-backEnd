package com.linkmoa.source.domain.favorite.dto.request;

import java.util.List;

import com.linkmoa.source.domain.directory.dto.response.DirectorySimpleResponse;
import com.linkmoa.source.domain.favorite.constant.FavoriteAction;
import com.linkmoa.source.domain.favorite.constant.ItemType;
import com.linkmoa.source.domain.site.dto.response.SiteSimpleResponse;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class FavoriteUpdateDto {

	public record Request(
		@NotNull Long itemId,
		@NotNull ItemType itemType

	) {
	}

	@Builder
	public record SimpleResponse(
		Long itemId,
		ItemType itemType,
		FavoriteAction action
	) {
	}

	@Builder
	public record DetailResponse(
		String email,
		List<DirectorySimpleResponse> directorySimpleResponses,
		List<SiteSimpleResponse> siteSimpleResponses
	) {

	}
}
