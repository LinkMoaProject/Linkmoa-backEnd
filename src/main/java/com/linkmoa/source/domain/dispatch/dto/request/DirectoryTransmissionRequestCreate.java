package com.linkmoa.source.domain.dispatch.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import lombok.Builder;

@Builder
public record DirectoryTransmissionRequestCreate(
	BaseRequest baseRequest,
	String receiverEmail,
	Long directoryId
) {
}
