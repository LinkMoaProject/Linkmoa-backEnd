package com.linkmoa.source.domain.dispatch.dto.response;

import lombok.Builder;

@Builder
public record SharePageInvitationResponse(
	String receiverEmail,
	String senderEmail,
	String pageTitle,
	Long pageInvitationRequestId
) {
}
