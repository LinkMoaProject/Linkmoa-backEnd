package com.linkmoa.source.domain.notification.service;


import com.linkmoa.source.domain.notification.dto.response.NotificationSubscribeResponse;
import com.linkmoa.source.domain.notification.entity.Notification;
import com.linkmoa.source.domain.notification.error.NotificationErrorCode;
import com.linkmoa.source.domain.notification.exception.NotificationException;
import com.linkmoa.source.domain.notification.repository.NotificationRepository;
import com.linkmoa.source.domain.notification.repository.SseEmitterRepository;
import com.linkmoa.source.domain.notification.dto.response.NotificationResponse;
import com.linkmoa.source.domain.notification.constant.NotificationType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
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

        NotificationSubscribeResponse notificationSubscribeResponse =NotificationSubscribeResponse
                .builder()
                .userEmail(email)
                .countUnreadNotifications(notificationRepository.countUnreadNotificationsByReceiverEmail(email))
                .build();

        sendNotification(newEmitter,eventId,emitterId,notificationSubscribeResponse);

        if(hasLostData(lastEventId)){
            sendLostData(lastEventId,email,emitterId,newEmitter);
        }

        return newEmitter;

    }

    private void sendNotification(SseEmitter emitter,String eventId, String emitterId,Object notificationSubscribeResponse){
        try{
            // 1. SSE 이벤트 전송
           emitter.send(
                    SseEmitter.event()
                            .id(eventId) // 이벤트 ID
                            .name("SSE") // 이벤트 이름
                            .data(notificationSubscribeResponse) // 전송할 데이터 (text/event-stream)
            );
        }catch (IOException e){

            sseEmitterRepository.deleteById(emitterId);
        }
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
                .forEach(entry-> sendNotification(emitter,entry.getKey(),emitterId,entry.getValue()));
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
    public void send(Notification notification){

        // 1. Notify 엔티티 생성 및 저장
       // Notification notification = notificationRepository.save(notification);

        String receiverEmail =notification.getReceiverEmail();

        // 2. 이벤트 ID 생성
        String eventId = receiverEmail + "_" +System.currentTimeMillis();

        // 3. 수신자와 관련된 모든 SSE (emitters) 가져오기
        Map<String, SseEmitter> emitters = sseEmitterRepository.findAllEmitterStartWithByMemberId(receiverEmail);

        Long countUnreadNotifications = notificationRepository.countUnreadNotificationsByReceiverEmail(receiverEmail);

        // 4. 각 emitter에 알림 전송
        emitters.forEach(
                (key,emitter)->{
                    // 4-1. 이벤트 캐시 저장
                    sseEmitterRepository.saveEventCache(key,notification);

                    // 4-2. SSE 연결로 알림 전송
                    sendNotification(emitter,eventId,key, NotificationResponse.of(notification,countUnreadNotifications));
                }
        );

    }

    @Transactional
    public void deleteAllNotificationByMemberEmail(String email) {
        notificationRepository.deleteAllBySenderEmailOrReceiverEmail(email);
    }


}
