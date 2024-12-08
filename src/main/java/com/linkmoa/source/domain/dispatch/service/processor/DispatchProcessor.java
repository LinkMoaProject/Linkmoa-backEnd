package com.linkmoa.source.domain.dispatch.service.processor;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.dto.request.DispatchProcessingRequest;
import com.linkmoa.source.domain.dispatch.dto.response.ApiDispatchResponseSpec;
import com.linkmoa.source.domain.dispatch.error.DispatchErrorCode;
import com.linkmoa.source.domain.dispatch.exception.DispatchException;
import com.linkmoa.source.domain.notify.constant.NotificationType;

public interface DispatchProcessor {
    ApiDispatchResponseSpec<?> processRequest(DispatchProcessingRequest dispatchProcessingRequest,PrincipalDetails principalDetails);

    default void validateRequestStatus(RequestStatus status) {
        if (status == RequestStatus.ACCEPTED || status == RequestStatus.REJECTED) {
            throw new DispatchException(DispatchErrorCode.REQUEST_ALREADY_PROCESSED);
        }
    }

    default void validateNotificationType(NotificationType expectedType, NotificationType actualType) {
        if (expectedType != actualType) {
            throw new DispatchException(DispatchErrorCode.INVALID_NOTIFICATION_TYPE);
        }
    }

    default void validateReceiver(String expectedReceiver, String currentMemberEmail) {
        if (!expectedReceiver.equals(currentMemberEmail)) {
            throw new DispatchException(DispatchErrorCode.INVALID_RECEIVER);
        }
    }

}
