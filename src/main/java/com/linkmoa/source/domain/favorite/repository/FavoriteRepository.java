package com.linkmoa.source.domain.favorite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkmoa.source.domain.favorite.constant.ItemType;
import com.linkmoa.source.domain.favorite.entity.Favorite;
import com.linkmoa.source.domain.member.entity.Member;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {

	Favorite findByItemIdAndItemType(Long itemId, ItemType itemType);

	List<Favorite> findByMember(Member member);

}
