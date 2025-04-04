package com.linkmoa.source.domain.favorite.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FavoriteAction {
	ADDED("아이템을 즐겨찾기에 등록했습니다."),
	REMOVED("아이템을 즐겨찾기에서 삭제했습니다.");
	private final String message;
}
