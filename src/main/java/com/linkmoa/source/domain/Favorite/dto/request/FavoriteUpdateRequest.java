package com.linkmoa.source.domain.Favorite.dto.request;

import com.linkmoa.source.domain.Favorite.constant.ItemType;
import jakarta.validation.constraints.NotNull;

public record FavoriteUpdateRequest(
        @NotNull Long itemId,
        @NotNull ItemType itemType

) {
}
