package com.linkmoa.source.domain.notification.repository;

public interface NotificationRepositoryCustom {

	Long updateUnreadNotificationsToReadByReceiverEmail(String receiverEmail);

	Long countUnreadNotificationsByReceiverEmail(String receiverEmail);

}
