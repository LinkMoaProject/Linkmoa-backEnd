package com.linkmoa.source.domain.notify.dto.response;


import com.linkmoa.source.domain.notify.entity.Notify;
import lombok.Builder;

@Builder
public record NotifyResponse(
        String content,
        String notificationType,
        String receiverEmail,
        String sendrEmail
) {

    public static NotifyResponse of(Notify notify){
        return NotifyResponse.builder()
                .content(notify.getContent())
                .notificationType(String.valueOf(notify.getNotificationType()))
                .receiverEmail(notify.getReceiverEmail())
                .sendrEmail(notify.getSenderEmail())
                .build();
    }

}
