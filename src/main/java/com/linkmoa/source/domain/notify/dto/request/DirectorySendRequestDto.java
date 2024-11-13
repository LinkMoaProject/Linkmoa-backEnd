package com.linkmoa.source.domain.notify.dto.request;


import lombok.Builder;

@Builder
public record DirectorySendRequestDto(
        String senderEmail,
        String receiverEmail,
        Long directoryId
) {
}
