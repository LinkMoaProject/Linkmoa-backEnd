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
    public List<Notification> findUnreadNotificationsByReceiverEmail(String receiverEmail) {

        return jpaQueryFactory
                .selectFrom(notification)
                .where(
                        notification.isRead.eq(false).and
                        (notification.receiverEmail.eq(receiverEmail))
                ).fetch();
    }

}
