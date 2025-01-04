package com.linkmoa.source.domain.memberPageLink.repository;

import org.springframework.data.domain.Page;

public interface MemberPageLinkRepositoryCustom {


    Page findPersonalPageByMemberId(Long memberId);

}
