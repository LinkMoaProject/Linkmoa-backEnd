package com.linkmoa.source.domain.notification.repository;

import com.linkmoa.source.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {

    Long updateUnreadNotificationsToReadByReceiverEmail(String receiverEmail);

}
