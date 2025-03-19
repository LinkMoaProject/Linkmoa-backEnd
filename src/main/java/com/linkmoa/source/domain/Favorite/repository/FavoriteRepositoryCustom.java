package com.linkmoa.source.domain.Favorite.repository;

import com.linkmoa.source.domain.member.entity.Member;

public interface FavoriteRepositoryCustom {
	public void incrementOrderIndexesForMember(Member member);

	public void decrementFavoriteOrderIndexes(Integer orderIndex);

}
