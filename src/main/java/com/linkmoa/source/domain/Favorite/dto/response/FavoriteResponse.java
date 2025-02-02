package com.linkmoa.source.domain.Favorite.dto.response;

import com.linkmoa.source.domain.Favorite.constant.FavoriteType;
import lombok.Builder;

@Builder
public record FavoriteResponse(
        Long itemId,
        FavoriteType favoriteType

) {
}
