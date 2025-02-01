package com.linkmoa.source.domain.Favorite.repository;

import com.linkmoa.source.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.linkmoa.source.domain.Favorite.entity.QFavorite.favorite;


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
    
}
