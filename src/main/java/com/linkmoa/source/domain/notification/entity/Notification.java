package com.linkmoa.source.domain.notification.entity;


import com.linkmoa.source.domain.notification.constant.NotificationType;
import com.linkmoa.source.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class Notification extends BaseEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notice_id")
    private Long id;

    @Column(name="is_read",nullable = false)
    private boolean isRead;

    @Column(name="notice_content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(name="receiver_email")
    private String receiverEmail;

    @Column(name="sender_email")
    private String senderEmail;

    @Column(name="request_id")
    private Long requestId;

    @Builder
    public Notification(String receiverEmail,String senderEmail, NotificationType notificationType,String content,boolean isRead,Long requestId){
        this.isRead=isRead;
        this.content=content;
        this.notificationType=notificationType;
        this.receiverEmail=receiverEmail;
        this.senderEmail=senderEmail;
        this.requestId = requestId;
    }

    public void markToRead(){
        this.isRead=true;
    }





}
