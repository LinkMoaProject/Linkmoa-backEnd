package com.linkmoa.source.domain.notification.dto.response;

import lombok.Builder;

@Builder
public record NotificationSubscribeResponse(
        String userEmail,
        Long countUnreadNotifications
) {
}
