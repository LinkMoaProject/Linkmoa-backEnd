package com.linkmoa.source.domain.page.dto.response;

import com.linkmoa.source.global.constant.RequestStatus;
import lombok.Builder;

@Builder
public record SharePageInvitationActionResponse(
        Long sharePageInvitationRequestId,
        String senderEmail,
        RequestStatus requestStatus

) {
}
