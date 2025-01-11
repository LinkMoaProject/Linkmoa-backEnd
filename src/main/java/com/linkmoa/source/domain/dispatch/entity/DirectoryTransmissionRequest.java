package com.linkmoa.source.domain.dispatch.entity;


import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.notification.aop.proxy.NotificationInfo;

import com.linkmoa.source.domain.notification.constant.NotificationType;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class DirectoryTransmissionRequest extends BaseEntity implements NotificationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="directory_send_request_id")
    private Long DirectorySendRequestId;

    @Column(name="receiver_email")
    private String receiverEmail;

    @Column(name="sender_email")
    private String senderEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType = NotificationType.SEND_DIRECTORY;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus requestStatus = RequestStatus.WAITING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directory_id", nullable = false)
    private Directory directory;


    @Builder
    public DirectoryTransmissionRequest(String receiverEmail, String senderEmail, Directory directory) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.directory = directory;
    }

    @Override
    public String getSenderEmail() {
        return senderEmail;
    }

    @Override
    public String getReceiverEmail() {
        return receiverEmail;
    }

    @Override
    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void changeDirectorySendRequestStatus(RequestStatus newStatus) {
        if (this.requestStatus == RequestStatus.ACCEPTED) {
            throw new DirectoryException(DirectoryErrorCode.REQUEST_ALREADY_ACCEPTED);
        }
        this.requestStatus = newStatus;
    }
}
