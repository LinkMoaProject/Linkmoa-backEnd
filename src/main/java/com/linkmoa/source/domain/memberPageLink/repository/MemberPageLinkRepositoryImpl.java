package com.linkmoa.source.domain.memberPageLink.repository;


import com.linkmoa.source.domain.page.contant.PageType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.linkmoa.source.domain.memberPageLink.entity.QMemberPageLink.memberPageLink;
import static com.linkmoa.source.domain.page.entity.QPage.page;
import com.linkmoa.source.domain.page.entity.Page;
@RequiredArgsConstructor
public class MemberPageLinkRepositoryImpl implements MemberPageLinkRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    public Page findPersonalPageByMemberId(Long memberId) {

        return jpaQueryFactory
                .select(memberPageLink.page)
                .from(memberPageLink)
                .where(
                        memberPageLink.member.id.eq(memberId),
                        memberPageLink.page.pageType.eq(PageType.PERSONAL)
                )
                .fetchOne();

    }

    @Override
    public Long findPersonalPageIdByMemberIdAndPermissionType(Long memberId) {

        Long personalPageId = jpaQueryFactory.select(page.id)
                .from(memberPageLink)
                .join(memberPageLink.page, page)
                .where(memberPageLink.member.id.eq(memberId)
                        .and(page.pageType.eq(PageType.PERSONAL)))
                .fetchOne();

        return personalPageId;
    }
}
