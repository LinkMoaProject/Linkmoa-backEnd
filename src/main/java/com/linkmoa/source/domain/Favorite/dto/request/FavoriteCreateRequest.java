package com.linkmoa.source.domain.Favorite.dto.request;

import com.linkmoa.source.domain.Favorite.constant.FavoriteType;

public record FavoriteCreateRequest(
        Long itemId,
        FavoriteType favoriteType

) {
}
