package com.linkmoa.source.domain.page.dto.response;

import lombok.Builder;

@Builder
public record PageInvitationRequestCreateResponse(
        String receiverEmail,
        String senderEmail,
        String pageTitle
) {
}
