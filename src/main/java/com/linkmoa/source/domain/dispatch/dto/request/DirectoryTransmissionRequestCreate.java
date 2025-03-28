package com.linkmoa.source.domain.dispatch.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DirectoryTransmissionRequestCreate(
	BaseRequest baseRequest,
	@NotNull String receiverEmail,
	@NotNull Long directoryId
) {
}
