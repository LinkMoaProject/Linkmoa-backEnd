package com.linkmoa.source.domain.notification.dto.response;

import lombok.Builder;

@Builder
public record UnreadNotificationCountResponse(
        String userEmail,
        Long countUnreadNotifications
) {
}
