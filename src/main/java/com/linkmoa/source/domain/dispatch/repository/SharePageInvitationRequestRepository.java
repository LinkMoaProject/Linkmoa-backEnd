package com.linkmoa.source.domain.dispatch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.entity.SharePageInvitationRequest;
import com.linkmoa.source.domain.dispatch.repository.custom.SharePageInvitationRequestRepositoryCustom;

public interface SharePageInvitationRequestRepository
	extends JpaRepository<SharePageInvitationRequest, Long>, SharePageInvitationRequestRepositoryCustom {
	Optional<SharePageInvitationRequest> findByPageIdAndRequestStatus(Long pageId, RequestStatus requestStatus);
}
