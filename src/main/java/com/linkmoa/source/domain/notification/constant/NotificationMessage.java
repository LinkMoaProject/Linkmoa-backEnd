package com.linkmoa.source.domain.notification.constant;

import lombok.Getter;

@Getter
public enum NotificationMessage {

	SEND_DIRECTORY_REQUEST("디렉토리 전송 요청이 있습니다."),
	INVITE_PAGE_REQUEST("공유 페이지 초대 요청이 있습니다.");
	private String message;

	NotificationMessage(String message) {
		this.message = message;
	}

	public static String getMessageByType(NotificationType notificationType) {
		switch (notificationType) {
			case TRANSMIT_DIRECTORY:
				return SEND_DIRECTORY_REQUEST.getMessage();
			case INVITE_PAGE:
				return INVITE_PAGE_REQUEST.getMessage();
			default:
				throw new IllegalArgumentException("매칭되는 알람 타입이 없습니다 : " + notificationType);
		}
	}

}
