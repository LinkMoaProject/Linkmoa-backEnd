package com.linkmoa.source.domain.notification.entity;

import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.notification.constant.NotificationType;
import com.linkmoa.source.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
public class Notification extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@Column(name = "is_read", nullable = false)
	private boolean isRead;

	@Column(name = "content", length = 120)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "notification_type")
	private NotificationType notificationType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	private Member sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", nullable = false)
	private Member receiver;

	@Column(name = "request_id")
	private Long requestId;

	@Builder
	public Notification(Member receiver, Member sender, NotificationType notificationType, String content,
		boolean isRead, Long requestId) {
		this.isRead = isRead;
		this.content = content;
		this.notificationType = notificationType;
		this.receiver = receiver;
		this.sender = sender;
		this.requestId = requestId;
	}

	public void markToRead() {
		this.isRead = true;
	}

}
