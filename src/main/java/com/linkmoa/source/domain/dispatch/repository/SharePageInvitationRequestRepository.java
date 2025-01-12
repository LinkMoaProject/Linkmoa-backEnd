package com.linkmoa.source.domain.dispatch.repository;

import com.linkmoa.source.domain.dispatch.entity.SharePageInvitationRequest;
import com.linkmoa.source.domain.dispatch.repository.custom.SharePageInvitationRequestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharePageInvitationRequestRepository extends JpaRepository<SharePageInvitationRequest,Long>, SharePageInvitationRequestRepositoryCustom {
}
