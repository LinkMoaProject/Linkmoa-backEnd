package com.linkmoa.source.domain.notification.dto.response;


import com.linkmoa.source.domain.notification.entity.Notification;
import lombok.Builder;

@Builder
public record NotificationResponse(
        String content,
        String notificationType,
        String receiverEmail,
        String sendrEmail
) {

    public static NotificationResponse of(Notification notification){
        return NotificationResponse.builder()
                .content(notification.getContent())
                .notificationType(String.valueOf(notification.getNotificationType()))
                .receiverEmail(notification.getReceiverEmail())
                .sendrEmail(notification.getSenderEmail())
                .build();
    }

}
