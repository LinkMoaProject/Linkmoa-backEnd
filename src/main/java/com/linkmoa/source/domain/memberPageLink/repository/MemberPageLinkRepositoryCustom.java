package com.linkmoa.source.domain.memberPageLink.repository;

import com.linkmoa.source.domain.page.entity.Page;

public interface MemberPageLinkRepositoryCustom {


    Page findPersonalPageByMemberId(Long memberId);

}
