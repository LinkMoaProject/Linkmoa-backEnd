package com.linkmoa.source.domain.Favorite.repository;

import com.linkmoa.source.domain.Favorite.constant.ItemType;
import com.linkmoa.source.domain.Favorite.entity.Favorite;
import com.linkmoa.source.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite,Long>,FavoriteRepositoryCustom {

     Favorite findByItemIdAndItemType(Long itemId, ItemType itemType);
     List<Favorite> findByMember(Member member);

}
