package com.linkmoa.source.domain.Favorite.repository;

import com.linkmoa.source.domain.Favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite,Long>,FavoriteRepositoryCustom {
}
