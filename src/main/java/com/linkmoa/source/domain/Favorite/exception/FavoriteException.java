package com.linkmoa.source.domain.Favorite.exception;

import com.linkmoa.source.domain.Favorite.error.FavoriteErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FavoriteException extends RuntimeException {
	private final FavoriteErrorCode favoriteErrorCode;
}
