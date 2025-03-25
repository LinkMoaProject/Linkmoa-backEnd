package com.linkmoa.source.global.util;

import org.springframework.stereotype.Component;

import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.global.error.code.impl.ValidationErrorCode;
import com.linkmoa.source.global.exception.ValidationException;

@Component
public class RequestStatusUtil {

	public static void validateRequestStatus(RequestStatus currentStatus) {
		if (currentStatus == RequestStatus.ACCEPTED) {
			throw new ValidationException(ValidationErrorCode.REQUEST_ALREADY_ACCEPTED);
		} else if (currentStatus == RequestStatus.REJECTED) {
			throw new ValidationException(ValidationErrorCode.REQUEST_ALREADY_REJECTED);
		}
	}

	public static void changeRequestStatus(Object request, RequestStatus newStatus) {
		try {
			// 현재 상태 가져오기
			RequestStatus currentStatus = (RequestStatus)request.getClass()
				.getMethod("getRequestStatus")
				.invoke(request);

			validateRequestStatus(currentStatus);

			// 상태 변경 메서드 호출
			request.getClass()
				.getMethod("changeRequestStatus", RequestStatus.class)
				.invoke(request, newStatus);

		} catch (ReflectiveOperationException e) {
			throw new UnsupportedOperationException(
				"Request 객체에 적합한 상태 변경 메서드가 없습니다: " + request.getClass().getName(), e);
		}
	}
}

