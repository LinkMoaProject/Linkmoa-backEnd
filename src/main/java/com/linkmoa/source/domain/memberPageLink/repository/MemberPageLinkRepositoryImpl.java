package com.linkmoa.source.domain.memberPageLink.repository;


import com.linkmoa.source.domain.page.contant.PageType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.linkmoa.source.domain.memberPageLink.entity.QMemberPageLink.memberPageLink;
import static com.linkmoa.source.domain.page.entity.QPage.page;
import com.linkmoa.source.domain.page.entity.Page;

import java.util.Optional;

@RequiredArgsConstructor
public class MemberPageLinkRepositoryImpl implements MemberPageLinkRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    public Optional<Page> findPersonalPageByMemberId(Long memberId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(memberPageLink.page)
                        .from(memberPageLink)
                        .where(
                                memberPageLink.member.id.eq(memberId),
                                memberPageLink.page.pageType.eq(PageType.PERSONAL)
                        )
                        .fetchOne() // Page 또는 null 반환
        );
    }

}
