package com.linkmoa.source.domain.dispatch.service.processor;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.dto.response.ApiDispatchResponseSpec;
import com.linkmoa.source.domain.dispatch.error.DispatchErrorCode;

public interface DispatchProcessor {
    ApiDispatchResponseSpec<?> processRequest(Long requestId, RequestStatus status, PrincipalDetails principalDetails);

    // 기본 메서드로 상태 확인 로직 추가
    default void validateRequestStatus(RequestStatus status) {
        if (status == RequestStatus.ACCEPTED || status == RequestStatus.REJECTED) {
            throw new DispatchErrorCode(DispatchErrorCode.REQUESR_ALREADY_PROCESSED);
        }
    }

}
