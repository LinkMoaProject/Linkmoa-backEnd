package com.linkmoa.source.domain.dispatch.entity;

import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.notification.aop.proxy.NotificationInfo;
import com.linkmoa.source.domain.notification.constant.NotificationType;
import com.linkmoa.source.domain.page.entity.Page;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SharePageInvitationRequest extends BaseEntity implements NotificationInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "share_page_invite_request_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	private Member sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", nullable = false)
	private Member receiver;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "notification_type")
	private NotificationType notificationType = NotificationType.INVITE_PAGE;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "request_status")
	private RequestStatus requestStatus = RequestStatus.WAITING;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "page_id", nullable = false)
	private Page page;

	@Enumerated(EnumType.STRING)
	@Column(name = "permission_type")
	private PermissionType permissionType;

	@Builder
	public SharePageInvitationRequest(Member receiver, Member sender, Page page, PermissionType permissionType) {
		this.sender = sender;
		this.receiver = receiver;
		this.page = page;
		this.permissionType = permissionType;
	}

	public void changeRequestStatus(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	@Override
	public Member getReceiver() {
		return receiver;
	}

	@Override
	public Member getSender() {
		return sender;
	}

	@Override
	public NotificationType getNotificationType() {
		return notificationType;
	}

	@Override
	public Long getRequestId() {
		return id;
	}

}
