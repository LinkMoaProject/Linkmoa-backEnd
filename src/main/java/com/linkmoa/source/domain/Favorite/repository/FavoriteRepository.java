package com.linkmoa.source.domain.Favorite.repository;

import com.linkmoa.source.domain.Favorite.constant.FavoriteType;
import com.linkmoa.source.domain.Favorite.entity.Favorite;
import com.linkmoa.source.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite,Long>,FavoriteRepositoryCustom {

     Favorite findByItemIdAndFavoriteType(Long itemId, FavoriteType favoriteType);
     List<Favorite> findByMember(Member member);

}
