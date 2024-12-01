package com.linkmoa.source.domain.directory.dto.request;


import com.linkmoa.source.global.dto.request.BaseRequest;
import lombok.Builder;

@Builder
public record DirectoryTransmissionSendRequest(
        BaseRequest baseRequest,
        String receiverEmail,
        Long directoryId
) {
}
