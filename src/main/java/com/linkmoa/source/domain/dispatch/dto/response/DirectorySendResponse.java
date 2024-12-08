package com.linkmoa.source.domain.dispatch.dto.response;


import lombok.Builder;

@Builder
public record DirectorySendResponse(
        String receiverEmail,
        String senderEmail,
        String directoryName
) {
}
