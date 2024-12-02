package com.linkmoa.source.domain.dispatch.dto.request;

import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import lombok.Builder;

@Builder
public record SharePageInvitationActionRequest(
        Long sharePageInvitationRequestId,
        RequestStatus requestStatus,
        PermissionType permissionType

) {
}
