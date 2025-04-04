package com.linkmoa.source.domain.page.repository;

import static com.linkmoa.source.domain.memberPageLink.entity.QMemberPageLink.*;
import static com.linkmoa.source.domain.page.entity.QPage.*;

import java.util.List;

import com.linkmoa.source.domain.page.dto.response.PageResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PageRepositoryImpl implements PageRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private final EntityManager entityManager;

	@Override
	public List<PageResponse> findAllPagesByMemberId(Long memberId) {

		List<PageResponse> result = jpaQueryFactory.
			select(Projections.constructor(
				PageResponse.class,
				page.id,
				page.pageTitle,
				page.pageType
			))
			.from(memberPageLink)
			.where(memberIdEq(memberId))
			.join(page).on(memberPageLink.page.id.eq(page.id))
			.fetch();
		return result;
	}

	private BooleanExpression memberIdEq(Long memberId) {
		return memberId == null ? null : memberPageLink.member.id.eq(memberId);
	}

	public Long findRootDirectoryIdByPageId(Long pageId) {
		return jpaQueryFactory.
			select(page.rootDirectory.id)
			.from(page)
			.where(page.id.eq(pageId))
			.fetchOne();
	}

}
