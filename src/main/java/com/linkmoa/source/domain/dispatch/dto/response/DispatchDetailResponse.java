package com.linkmoa.source.domain.dispatch.dto.response;

import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.notify.constant.NotificationType;

public record DispatchDetailResponse(
        Long SharePageInvitationRequestId,
        String email,
        String message,
        RequestStatus requestStatus,
        NotificationType notificationType

) {
}
