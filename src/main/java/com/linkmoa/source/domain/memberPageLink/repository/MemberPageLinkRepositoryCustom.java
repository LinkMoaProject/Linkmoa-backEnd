package com.linkmoa.source.domain.memberPageLink.repository;

import java.util.Optional;

import com.linkmoa.source.domain.page.entity.Page;

public interface MemberPageLinkRepositoryCustom {

	Optional<Page> findPersonalPageByMemberId(Long memberId);

}
