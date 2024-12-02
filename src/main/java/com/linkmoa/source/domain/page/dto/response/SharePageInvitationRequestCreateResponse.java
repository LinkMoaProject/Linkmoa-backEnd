package com.linkmoa.source.domain.page.dto.response;

import lombok.Builder;

@Builder
public record SharePageInvitationRequestCreateResponse(
        String receiverEmail,
        String senderEmail,
        String pageTitle,
        Long PageInvitationRequestId
) {
}
