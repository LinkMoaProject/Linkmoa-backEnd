package com.linkmoa.source.domain.dispatch.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class DirectoryTransmissionDto {

	public record Requeest(
		BaseRequest baseRequest,
		@NotNull String receiverEmail,
		@NotNull Long directoryId
	) {

	}

	@Builder
	public record Response(
		String receiverEmail,
		String senderEmail,
		String directoryName,
		Long directoryTransmissionId
	) {

	}
}
