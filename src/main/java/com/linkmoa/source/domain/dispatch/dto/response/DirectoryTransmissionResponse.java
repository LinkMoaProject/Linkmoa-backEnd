package com.linkmoa.source.domain.dispatch.dto.response;


import lombok.Builder;

@Builder
public record DirectoryTransmissionResponse(
        String receiverEmail,
        String senderEmail,
        String directoryName,
        Long directoryTransmissionId
) {
}
