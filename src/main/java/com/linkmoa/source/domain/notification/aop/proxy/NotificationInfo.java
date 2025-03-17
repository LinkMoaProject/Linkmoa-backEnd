package com.linkmoa.source.domain.notification.aop.proxy;

import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.notification.constant.NotificationType;

public interface NotificationInfo {

    Member getSender();
    Member getReceiver();
    NotificationType getNotificationType();
    Long getRequestId();
}
