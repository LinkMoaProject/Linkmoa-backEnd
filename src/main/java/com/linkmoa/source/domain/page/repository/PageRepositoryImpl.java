package com.linkmoa.source.domain.page.repository;

import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.dto.response.PagesResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.linkmoa.source.domain.memberPageLink.entity.QMemberPageLink.memberPageLink;
import static com.linkmoa.source.domain.page.entity.QPage.page;


@RequiredArgsConstructor
public class PageRepositoryImpl implements PageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PagesResponse> findAllPagesByMemberId(Long memberId) {

        List<PagesResponse> result = jpaQueryFactory.
                select(Projections.constructor(
                        PagesResponse.class,
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

    private BooleanExpression memberIdEq(Long memberId){
        return memberId==null ? null : memberPageLink.member.id.eq(memberId);
    }

}
