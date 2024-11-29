package com.linkmoa.source.domain.directory.error;


import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.notify.aop.proxy.NotifyInfo;

import com.linkmoa.source.domain.notify.constant.NotificationType;
import com.linkmoa.source.global.constant.RequestStatus;
import com.linkmoa.source.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class DirectorySendRequest extends BaseEntity implements NotifyInfo {

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

    @ManyToOne(fetch = FetchType.LAZY) // Directory와의 관계 매핑
    @JoinColumn(name = "directory_id", nullable = false) // directory_id를 외래키로 설정
    private Directory directory;


    @Builder
    public DirectorySendRequest(String receiverEmail, String senderEmail, Directory directory) {
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
