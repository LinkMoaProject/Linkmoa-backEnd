package com.linkmoa.source.domain.dispatch.dto.request;


import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.notify.constant.NotificationType;
import lombok.Builder;
@Builder
public record DispatchProcessingRequest(
        Long requestId,
        RequestStatus requestStatus,
        NotificationType notificationType
) {
}
