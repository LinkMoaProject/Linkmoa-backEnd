package com.linkmoa.source.domain.page.dto.request;

import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.global.constant.RequestStatus;
import lombok.Builder;

@Builder
public record SharePageInvitationAction(
        Long sharePageInvitationRequestId,
        RequestStatus requestStatus,
        PermissionType permissionType

) {
}
