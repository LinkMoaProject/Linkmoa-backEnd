package com.linkmoa.source.domain.notify.dto.response;

import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.notify.constant.NotificationType;

public record NotificationDetailResponse(
        Long id,
        String email,
        String content,
        RequestStatus requestStatus,
        NotificationType notificationType

) {
}
