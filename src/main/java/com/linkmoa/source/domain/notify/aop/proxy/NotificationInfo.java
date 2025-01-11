package com.linkmoa.source.domain.notify.aop.proxy;

import com.linkmoa.source.domain.notify.constant.NotificationType;

public interface NotificationInfo {

    String getSenderEmail();
    String getReceiverEmail();
    NotificationType getNotificationType();
}
