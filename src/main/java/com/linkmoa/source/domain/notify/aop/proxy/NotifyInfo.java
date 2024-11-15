package com.linkmoa.source.domain.notify.aop.proxy;

import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.notify.constant.NotificationType;

public interface NotifyInfo {

    String getSenderEmail();    // 발신자 정보를 제공하는 메서드
    String getReceiverEmail();  // 수신자 정보를 제공하는 메서드 (오타 수정)

    NotificationType getNotificationType(); // 알림 유형을 제공하는 메서드
}
