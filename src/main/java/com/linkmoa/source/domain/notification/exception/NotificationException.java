package com.linkmoa.source.domain.notification.exception;

import com.linkmoa.source.domain.notification.error.NotificationErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationException extends RuntimeException {

	private final NotificationErrorCode notificationErrorCode;
}
