package com.linkmoa.source.domain.notify.dto.response;


import com.linkmoa.source.domain.notify.entity.Notify;
import lombok.Builder;

@Builder
public record NotifyResponseDto(
        String content,
        String notificationType,
        String receiverEmail,
        String sendrEmail
) {

    public static NotifyResponseDto of(Notify notify){
        return NotifyResponseDto.builder()
                .content(notify.getContent())
                .notificationType(String.valueOf(notify.getNotificationType()))
                .receiverEmail(notify.getReceiverEmail())
                .sendrEmail(notify.getSenderEmail())
                .build();
    }

}
