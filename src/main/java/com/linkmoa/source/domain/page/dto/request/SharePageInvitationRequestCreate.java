package com.linkmoa.source.domain.page.dto.request;

import com.linkmoa.source.global.dto.request.BaseRequest;
import lombok.Builder;


@Builder
public record SharePageInvitationRequestCreate(
        BaseRequest baseRequest,
        String receiverEmail

){
}
