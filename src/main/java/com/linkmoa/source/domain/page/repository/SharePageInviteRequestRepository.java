package com.linkmoa.source.domain.page.repository;

import com.linkmoa.source.domain.page.entity.SharePageInvitationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageInviteRequestRepository extends JpaRepository<SharePageInvitationRequest,Long> {
}
