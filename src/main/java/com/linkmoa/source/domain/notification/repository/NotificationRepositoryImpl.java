package com.linkmoa.source.domain.notification.repository;

import static com.linkmoa.source.domain.notification.entity.QNotification.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Long updateUnreadNotificationsToReadByReceiverEmail(String receiverEmail) {
		return jpaQueryFactory
			.update(notification)
			.set(notification.isRead, true)
			.where(
				notification.receiver.email.eq(receiverEmail)
					.and(notification.isRead.eq(false))
			)
			.execute();
	}

	@Override
	public Long countUnreadNotificationsByReceiverEmail(String receiverEmail) {
		return jpaQueryFactory
			.select(notification.count().coalesce(0L))
			.from(notification)
			.where(
				notification.receiver.email.eq(receiverEmail)
					.and(notification.isRead.eq(false))
			)
			.fetchOne();
	}

}
