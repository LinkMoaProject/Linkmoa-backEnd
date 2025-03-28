package com.linkmoa.source.domain.dispatch.dto.request;

import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.global.dto.request.BaseRequest;

import lombok.Builder;

@Builder
public record SharePageInvitationRequestCreate(
	BaseRequest baseRequest,
	String receiverEmail,
	PermissionType permissionType

) {
}
