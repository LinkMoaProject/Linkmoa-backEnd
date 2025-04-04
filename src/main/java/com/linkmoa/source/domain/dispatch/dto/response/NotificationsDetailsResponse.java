package com.linkmoa.source.domain.dispatch.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record NotificationsDetailsResponse(
	List<DispatchDetailResponse> SharePageInvitationRequests,
	List<DispatchDetailResponse> DirectoryTransmissionRequests
) {
}
