package com.linkmoa.source.domain.favorite.repository;

import static com.linkmoa.source.domain.favorite.entity.QFavorite.*;

import com.linkmoa.source.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public void incrementOrderIndexesForMember(Member member) {
		jpaQueryFactory.update(favorite)
			.set(favorite.orderIndex, favorite.orderIndex.add(1))
			.where(favorite.member.eq(member))
			.execute();
	}

	@Override
	public void decrementFavoriteOrderIndexes(Integer orderIndex) {
		jpaQueryFactory.update(favorite)
			.set(favorite.orderIndex, favorite.orderIndex.subtract(1))
			.where(favorite.orderIndex.gt(orderIndex))
			.execute();
	}

}
