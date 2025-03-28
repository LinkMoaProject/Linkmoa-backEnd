package com.linkmoa.source.domain.favorite.exception;

import com.linkmoa.source.domain.favorite.error.FavoriteErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FavoriteException extends RuntimeException {
	private final FavoriteErrorCode favoriteErrorCode;
}
