package com.linkmoa.source.domain.dispatch.dto.response;

import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.notify.constant.NotificationType;

public record DispatchDetailResponse(
        Long id,
        String email,
        RequestStatus requestStatus,
        NotificationType notificationType

) {
}
