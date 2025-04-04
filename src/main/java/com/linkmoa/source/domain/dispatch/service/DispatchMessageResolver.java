package com.linkmoa.source.domain.dispatch.service;

import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.notification.constant.NotificationType;

public class DispatchMessageResolver {

	public static String resolve(RequestStatus status, NotificationType type) {
		return switch (type) {
			case INVITE_PAGE -> switch (status) {
				case ACCEPTED -> "공유 페이지 초대를 수락했습니다.";
				case REJECTED -> "공유 페이지 초대를 거절했습니다.";
				default -> "공유 페이지 초대가 처리되었습니다.";
			};
			case TRANSMIT_DIRECTORY -> switch (status) {
				case ACCEPTED -> "디렉토리를 이동했습니다.";
				case REJECTED -> "디렉토리 전송을 거절했습니다.";
				default -> "디렉토리 전송이 처리되었습니다.";
			};
			default -> "요청이 처리되었습니다.";
		};

	}
}

