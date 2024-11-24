package com.linkmoa.source.domain.notify.entity;


import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.notify.aop.proxy.NotifyInfo;
import com.linkmoa.source.domain.notify.constant.NotificationType;
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

    /*@ManyToOne
    @JoinColumn(name="receiver_id")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private Member sender;
*/

    @Column(name="receiver_email")
    private String receiverEmail;

    @Column(name="sender_email")
    private String senderEmail;

    @Column(name="directory_id")
    private Long directoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType = NotificationType.SEND_DIRECTORY;


    @Builder
    public DirectorySendRequest(String receiverEmail,String senderEmail,Long directoryId){
        this.senderEmail=senderEmail;
        this.receiverEmail=receiverEmail;
        this.directoryId =directoryId;
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
}
