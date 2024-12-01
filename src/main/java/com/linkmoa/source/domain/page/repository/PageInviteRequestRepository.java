package com.linkmoa.source.domain.page.repository;

import com.linkmoa.source.domain.page.entity.PageInvitationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageInviteRequestRepository extends JpaRepository<PageInvitationRequest,Long> {
}
