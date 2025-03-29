package com.linkmoa.source.domain.site.repository;

import static com.linkmoa.source.domain.favorite.entity.QFavorite.*;
import static com.linkmoa.source.domain.site.entity.QSite.*;

import java.util.List;
import java.util.stream.Collectors;

import com.linkmoa.source.domain.favorite.constant.ItemType;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SiteRepositoryImpl implements SiteRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<SiteDetailResponse> findSitesDetails(Long directoryId, List<Long> favoriteSiteIds) {

		return jpaQueryFactory
			.selectFrom(site)
			.where(site.directory.id.eq(directoryId))
			.fetch()
			.stream()
			.map(s -> SiteDetailResponse.builder()
				.siteId(s.getId())
				.siteUrl(s.getSiteUrl())
				.siteName(s.getSiteName())
				.orderIndex(s.getOrderIndex())
				.isFavorite(favoriteSiteIds.contains(s.getId()))
				.build())
			.collect(Collectors.toList());

	}

	@Override
	public List<SiteDetailResponse> findFavoriteSites(List<Long> favoriteSiteIds) {

		return jpaQueryFactory
			.selectDistinct(
				Projections.constructor(
					SiteDetailResponse.class,
					site.id,
					site.siteName,
					site.siteUrl,
					favorite.orderIndex,
					Expressions.constant(true)
				)
			)
			.from(site)
			.join(favorite).on(site.id.eq(favorite.itemId)
				.and(favorite.itemType.eq(ItemType.SITE)))
			.where(favorite.itemId.in(favoriteSiteIds))
			.orderBy(favorite.orderIndex.asc())
			.fetch();
	}

}
