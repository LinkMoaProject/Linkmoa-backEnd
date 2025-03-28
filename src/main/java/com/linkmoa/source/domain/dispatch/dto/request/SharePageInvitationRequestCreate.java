package com.linkmoa.source.domain.dispatch.dto.request;

import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.global.dto.request.BaseRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SharePageInvitationRequestCreate(
	BaseRequest baseRequest,
	@NotNull String receiverEmail,
	PermissionType permissionType

) {
}
