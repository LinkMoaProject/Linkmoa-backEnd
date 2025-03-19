package com.linkmoa.source.domain.dispatch.entity;

import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.notification.aop.proxy.NotificationInfo;

import com.linkmoa.source.domain.notification.constant.NotificationType;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.notification.entity.Notification;
import com.linkmoa.source.global.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import org.checkerframework.checker.units.qual.C;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DirectoryTransmissionRequest extends BaseEntity implements NotificationInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "directory_send_request_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	private Member sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", nullable = false)
	private Member receiver;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType notificationType = NotificationType.TRANSMIT_DIRECTORY;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RequestStatus requestStatus = RequestStatus.WAITING;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "directory_id", nullable = false)
	private Directory directory;

	@Builder
	public DirectoryTransmissionRequest(Member sender, Member receiver, Directory directory) {
		this.sender = sender;
		this.receiver = receiver;
		this.directory = directory;
	}

	@Override
	public Member getSender() {
		return sender;
	}

	@Override
	public Member getReceiver() {
		return receiver;
	}

	@Override
	public NotificationType getNotificationType() {
		return notificationType;
	}

	@Override
	public Long getRequestId() {
		return id;
	}

	public void changeDirectoryTransmissionRequestStatus(RequestStatus newStatus) {
		if (this.requestStatus == RequestStatus.ACCEPTED) {
			throw new DirectoryException(DirectoryErrorCode.REQUEST_ALREADY_ACCEPTED);
		}
		this.requestStatus = newStatus;
	}
}
