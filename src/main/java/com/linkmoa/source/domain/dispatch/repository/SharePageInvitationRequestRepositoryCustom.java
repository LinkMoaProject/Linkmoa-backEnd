package com.linkmoa.source.domain.dispatch.repository;

import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;

import java.util.List;

public interface SharePageInvitationRequestRepositoryCustom {

    List<DispatchDetailResponse> findAllSharePageInvitationRequestByMemberId(Long memberId);

}
