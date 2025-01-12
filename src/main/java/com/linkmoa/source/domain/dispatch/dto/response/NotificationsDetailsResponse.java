package com.linkmoa.source.domain.dispatch.dto.response;

import com.linkmoa.source.global.dto.request.BaseRequest;
import lombok.Builder;

import java.util.List;

@Builder
public record NotificationsDetailsResponse(

        List<DispatchDetailResponse>SharePageInvitationRequests ,
        List<DispatchDetailResponse>DirectoryTransmissionRequests
) {
}
