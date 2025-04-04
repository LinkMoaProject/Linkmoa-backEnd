package com.linkmoa.source.domain.dispatch.repository.custom;

import java.util.List;

import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;

public interface SharePageInvitationRequestRepositoryCustom {

	List<DispatchDetailResponse> findAllSharePageInvitationsByReceiverEmail(String receiverEmail);

}
