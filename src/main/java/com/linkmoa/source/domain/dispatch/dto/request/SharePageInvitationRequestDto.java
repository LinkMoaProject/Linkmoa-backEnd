package com.linkmoa.source.domain.dispatch.dto.request;

import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class SharePageInvitationRequestDto {
	@Builder
	public record Request(
		BaseRequest baseRequest,
		@NotNull String receiverEmail,
		PermissionType permissionType

	) {

	}

	@Builder
	public record Response(
		String receiverEmail,
		String senderEmail,
		String pageTitle,
		Long pageInvitationRequestId
	) {

	}
}




