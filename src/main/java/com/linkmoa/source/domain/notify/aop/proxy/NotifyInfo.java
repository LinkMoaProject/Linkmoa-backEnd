package com.linkmoa.source.domain.notify.aop.proxy;

import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.notify.constant.NotificationType;

public interface NotifyInfo {

    String getSenderEmail();
    String getReceiverEmail();
    NotificationType getNotificationType();
}
