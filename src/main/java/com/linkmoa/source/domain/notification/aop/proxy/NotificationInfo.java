package com.linkmoa.source.domain.notification.aop.proxy;

import com.linkmoa.source.domain.notification.constant.NotificationType;

public interface NotificationInfo {

    String getSenderEmail();
    String getReceiverEmail();
    NotificationType getNotificationType();
}
