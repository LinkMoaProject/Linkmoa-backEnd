package com.linkmoa.source.domain.favorite.dto.request;

import com.linkmoa.source.domain.favorite.constant.ItemType;

import jakarta.validation.constraints.NotNull;

public record FavoriteUpdateRequest(
	@NotNull Long itemId,
	@NotNull ItemType itemType

) {
}
