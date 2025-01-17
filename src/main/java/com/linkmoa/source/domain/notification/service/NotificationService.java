package com.linkmoa.source.domain.notification.service;


import com.linkmoa.source.domain.notification.dto.response.UnreadNotificationCountResponse;
import com.linkmoa.source.domain.notification.entity.Notification;
import com.linkmoa.source.domain.notification.repository.NotificationRepository;
import com.linkmoa.source.domain.notification.repository.SseEmitterRepository;
import com.linkmoa.source.domain.notification.dto.response.NotificationResponse;
import com.linkmoa.source.domain.notification.constant.NotificationType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT=(60L*1000*60)*6; // 6시간

    private final SseEmitterRepository sseEmitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(final String email,String lastEventId){
        String emitterId = makeTimeIncludeId(email);

        SseEmitter newEmitter = sseEmitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // newEmitter의 콜백 메소드
        newEmitter.onCompletion(()-> sseEmitterRepository.deleteById(emitterId));
        newEmitter.onTimeout(() -> sseEmitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(email);

        UnreadNotificationCountResponse unreadNotificationCountResponse = NotificationResponse.toUnreadNotificationCountResponse
                (email, notificationRepository.countUnreadNotificationsByReceiverEmail(email));

        sendNotificationWithUnreadCount(newEmitter,eventId,emitterId, unreadNotificationCountResponse);

        if(hasLostData(lastEventId)){
            sendLostData(lastEventId,email,emitterId,newEmitter);
        }

        return newEmitter;

    }



    private String makeTimeIncludeId(final String email){
        return email + "_"+System.currentTimeMillis();
    }

    private boolean hasLostData(String lastEventId){
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId,final String email,String emitterId,SseEmitter emitter){
        Map<String,Object> eventCaches = sseEmitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(email));
        eventCaches.entrySet().stream()
                .filter(entry-> lastEventId.compareTo(entry.getKey()) <0)
                .forEach(entry-> sendNotificationWithUnreadCount(emitter,entry.getKey(),emitterId,entry.getValue()));
    }


    public Notification createRequestNotification(String receiverEmail,String senderEmail, NotificationType notificationType,String content,Long requestId){


        return notificationRepository.save(
                Notification.builder()
                .receiverEmail(receiverEmail)
                .senderEmail(senderEmail)
                .notificationType(notificationType)
                .content(content)
                .isRead(false)
                .requestId(requestId)
                .build());
    }

    private void sendNotificationWithUnreadCount(SseEmitter emitter,String eventId, String emitterId,Object notificationSubscribeResponse){
        try{
            // 1. SSE 이벤트 전송
            emitter.send(
                    SseEmitter.event()
                            .id(eventId) // 이벤트 ID
                            .name("로그인 시, SSE 연결 완료") // 이벤트 이름
                            .data(notificationSubscribeResponse) // 전송할 데이터 (text/event-stream)
            );
        }catch (IOException e){

            sseEmitterRepository.deleteById(emitterId);
        }
    }

    public void sendUnreadNotificationCount(String receiverEmail){
        String eventId =makeTimeIncludeId(receiverEmail);
        Map<String, SseEmitter> emitters = sseEmitterRepository.findAllEmitterStartWithByMemberId(receiverEmail);
        Long countUnreadNotifications = notificationRepository.countUnreadNotificationsByReceiverEmail(receiverEmail);

        UnreadNotificationCountResponse unreadNotificationCountResponse = NotificationResponse
                .toUnreadNotificationCountResponse(receiverEmail, countUnreadNotifications);

        emitters.forEach(
                (key,emitter)->{
                    sendNotificationWithUnreadCount(emitter,eventId,key, unreadNotificationCountResponse);
                }
        );
    }

    public void sendNotificationDetails(Notification notification){

        String receiverEmail =notification.getReceiverEmail();

        // 1. 이벤트 ID 생성
        String eventId =makeTimeIncludeId(receiverEmail);

        // 2. 수신자와 관련된 모든 SSE (emitters) 가져오기
        Map<String, SseEmitter> emitters = sseEmitterRepository.findAllEmitterStartWithByMemberId(receiverEmail);

        Long countUnreadNotifications = notificationRepository.countUnreadNotificationsByReceiverEmail(receiverEmail);

        NotificationResponse notificationResponse = NotificationResponse.of(notification, countUnreadNotifications);


        // 3. 각 emitter에 알림 전송
        emitters.forEach(
                (key,emitter)->{
                    // 3-1. 이벤트 캐시 저장
                    sseEmitterRepository.saveEventCache(key,notification);

                    // 3-2. SSE 연결로 알림 전송
                    sendNotificationWithUnreadCount(emitter,eventId,key, notificationResponse);
                }
        );

    }

    @Transactional
    public void deleteAllNotificationByMemberEmail(String email) {
        notificationRepository.deleteAllBySenderEmailOrReceiverEmail(email);
    }

    public void temp() {
        System.out.println("test");
    }



}
