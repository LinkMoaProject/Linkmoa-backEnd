package com.linkmoa.source.domain.dispatch.service.processor;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.dto.response.ApiDispatchResponseSpec;

public interface DispatchProcessor {
    ApiDispatchResponseSpec<?> processRequest(Long requestId, RequestStatus status, PrincipalDetails principalDetails);

}
