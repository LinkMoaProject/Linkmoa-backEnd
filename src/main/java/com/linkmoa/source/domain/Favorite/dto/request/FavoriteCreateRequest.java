package com.linkmoa.source.domain.Favorite.dto.request;

import com.linkmoa.source.domain.Favorite.constant.FavoriteType;
import jakarta.validation.constraints.NotNull;

public record FavoriteCreateRequest(
        @NotNull Long itemId,
        @NotNull FavoriteType favoriteType

) {
}
