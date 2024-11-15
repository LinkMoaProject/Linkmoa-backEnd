package com.linkmoa.source.domain.notify.entity;


import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.notify.constant.NotificationType;
import com.linkmoa.source.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class Notify extends BaseEntity {

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
/*
    @ManyToOne
    @JoinColumn(name="member_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Member receiver;*/

    @Column(name="receiver_email")
    private String receiverEmail;

    @Column(name="sender_email")
    private String senderEmail;


    @Builder
    public Notify(String receiverEmail,String senderEmail, NotificationType notificationType,String content,boolean isRead){
        this.isRead=isRead;
        this.content=content;
        this.notificationType=notificationType;
        this.receiverEmail=receiverEmail;
        this.senderEmail=senderEmail;
    }





}
