package com.linkmoa.source.domain.notification.dto.response;


import com.linkmoa.source.domain.notification.entity.Notification;
import lombok.Builder;

@Builder
public record NotificationResponse(
        String content,
        String notificationType,
        String receiverEmail,
        String sendrEmail,
        Long countUnreadNotifications
) {

    public static NotificationResponse of(Notification notification,Long countUnreadNotifications){
        return NotificationResponse.builder()
                .content(notification.getContent())
                .notificationType(String.valueOf(notification.getNotificationType()))
                .receiverEmail(notification.getReceiverEmail())
                .sendrEmail(notification.getSenderEmail())
                .countUnreadNotifications(countUnreadNotifications)
                .build();
    }

    // NotificationSubscribeResponse 생성
    public static UnreadNotificationCountResponse toUnreadNotificationCountResponse(String userEmail, Long countUnreadNotifications) {
        return UnreadNotificationCountResponse.builder()
                .userEmail(userEmail)
                .countUnreadNotifications(countUnreadNotifications)
                .build();
    }

}
