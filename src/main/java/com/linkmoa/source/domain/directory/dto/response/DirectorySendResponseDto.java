package com.linkmoa.source.domain.directory.dto.response;


import lombok.Builder;

@Builder
public record DirectorySendResponseDto(
        String receiverEmail,
        String senderEmail,
        String directoryName
) {
}
