package com.linkmoa.source.domain.page.entity;


import com.linkmoa.source.domain.notify.aop.proxy.NotifyInfo;
import com.linkmoa.source.domain.notify.constant.NotificationType;
import com.linkmoa.source.global.constant.RequestStatus;
import com.linkmoa.source.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PageInvitationRequest extends BaseEntity implements NotifyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="page_invite_request_id")
    private Long PageInviteRequest;

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


    @Builder
    PageInvitationRequest(String receiverEmail, String senderEmail, Page page){
        this.senderEmail=senderEmail;
        this.receiverEmail=receiverEmail;
        this.page=page;
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
