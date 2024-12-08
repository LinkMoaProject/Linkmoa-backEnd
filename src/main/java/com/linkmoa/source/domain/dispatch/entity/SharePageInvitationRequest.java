package com.linkmoa.source.domain.dispatch.entity;


import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.notify.aop.proxy.NotifyInfo;
import com.linkmoa.source.domain.notify.constant.NotificationType;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SharePageInvitationRequest extends BaseEntity implements NotifyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="share_page_invite_request_id")
    private Long id;

    @Column(name="receiver_email")
    private String receiverEmail;

    @Column(name="sender_email")
    private String senderEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType = NotificationType.INVITE_PAGE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus requestStatus = RequestStatus.WAITING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;

    @Enumerated(EnumType.STRING)
    @Column(name="permission_type")
    private PermissionType permissionType;


    @Builder
    public SharePageInvitationRequest(String receiverEmail, String senderEmail, Page page, PermissionType permissionType){
        this.senderEmail=senderEmail;
        this.receiverEmail=receiverEmail;
        this.page=page;
        this.permissionType =permissionType;
    }

    public void changeRequestStatus(RequestStatus requestStatus){
        this.requestStatus=requestStatus;
    }

    @Override
    public String getReceiverEmail() {
        return receiverEmail;
    }
    @Override
    public String getSenderEmail() {
        return senderEmail;
    }
    @Override
    public NotificationType getNotificationType() {
        return notificationType;
    }


}
