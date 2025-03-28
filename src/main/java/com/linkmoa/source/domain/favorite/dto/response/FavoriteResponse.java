package com.linkmoa.source.domain.favorite.dto.response;

import com.linkmoa.source.domain.favorite.constant.ItemType;

import lombok.Builder;

@Builder
public record FavoriteResponse(
	Long itemId,
	ItemType itemType

) {
}
