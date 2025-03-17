package com.linkmoa.source.domain.memberPageLink.repository;

import com.linkmoa.source.domain.page.entity.Page;

import java.util.Optional;

public interface MemberPageLinkRepositoryCustom {

    Optional<Page> findPersonalPageByMemberId(Long memberId);

}
