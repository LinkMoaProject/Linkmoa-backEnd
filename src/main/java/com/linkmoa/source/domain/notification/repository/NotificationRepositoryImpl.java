package com.linkmoa.source.domain.notification.repository;

import com.linkmoa.source.domain.notification.entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.linkmoa.source.domain.notification.entity.QNotification.notification;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long updateUnreadNotificationsToReadByReceiverEmail(String receiverEmail) {
        return jpaQueryFactory
                .update(notification)
                .set(notification.isRead, true)
                .where(
                        notification.receiverEmail.eq(receiverEmail)
                        .and(notification.isRead.eq(false))
                )
                .execute();
    }

}
