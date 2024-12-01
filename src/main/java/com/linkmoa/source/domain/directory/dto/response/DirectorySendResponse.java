package com.linkmoa.source.domain.directory.dto.response;


import lombok.Builder;

@Builder
public record DirectorySendResponse(
        String receiverEmail,
        String senderEmail,
        String directoryName
) {
}
