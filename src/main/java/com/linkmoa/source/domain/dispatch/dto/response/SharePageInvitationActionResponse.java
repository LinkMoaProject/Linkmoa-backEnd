package com.linkmoa.source.domain.dispatch.dto.response;

import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import lombok.Builder;

@Builder
public record SharePageInvitationActionResponse(
        Long sharePageInvitationRequestId,
        String senderEmail,
        RequestStatus requestStatus

) {
}
