package com.linkmoa.source.domain.notify.service;


import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.notify.dto.response.NotifyResponseDto;
import com.linkmoa.source.domain.notify.constant.NotificationType;
import com.linkmoa.source.domain.notify.entity.Notify;
import com.linkmoa.source.domain.notify.repository.NotifyRepository;
import com.linkmoa.source.domain.notify.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotifyService {
    private static final Long DEFAULT_TIMEOUT=(60L*1000*60)*6; // 6시간

    private final SseEmitterRepository sseEmitterRepository;
    private final NotifyRepository notifyRepository;

    public SseEmitter subscribe(final String email,String lastEventId){
        String emitterId = makeTimeIncludeId(email);

        SseEmitter newEmitter = sseEmitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // newEmitter의 콜백 메소드
        newEmitter.onCompletion(()-> sseEmitterRepository.deleteById(emitterId));
        newEmitter.onTimeout(() -> sseEmitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(email);
        sendNotification(newEmitter,eventId,emitterId,"연결 완료됐습니다. [user Email = "+email+"]");

        if(hasLostData(lastEventId)){
            sendLostData(lastEventId,email,emitterId,newEmitter);
        }

        return newEmitter;

    }

    private void sendNotification(SseEmitter emitter,String eventId, String emitterId,Object data){
        try{
            // 1. SSE 이벤트 전송
           emitter.send(
                    SseEmitter.event()
                            .id(eventId) // 이벤트 ID
                            .name("SSE") // 이벤트 이름
                            .data(data) // 전송할 데이터 (text/event-stream)
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


    public void send(String receiverEmail,String senderEmail, NotificationType notificationType, String content, String url){

        // 1. Notify 엔티티 생성 및 저장
        Notify notification = notifyRepository.save(createNotification(receiverEmail,senderEmail,notificationType,content));

        // 2. 이벤트 ID 생성
        String eventId = receiverEmail + "_" +System.currentTimeMillis();

        // 3. 수신자와 관련된 모든 SSE (emitters) 가져오기
        Map<String, SseEmitter> emitters = sseEmitterRepository.findAllEmitterStartWithByMemberId(receiverEmail);

        // 4. 각 emitter에 알림 전송
        emitters.forEach(
                (key,emitter)->{
                    // 4-1. 이벤트 캐시 저장
                    sseEmitterRepository.saveEventCache(key,notification);

                    // 4-2. SSE 연결로 알림 전송
                    sendNotification(emitter,eventId,key, NotifyResponseDto.of(notification));
                }
        );

    }

    private Notify createNotification(String receiverEmail,String senderEmail, NotificationType notificationType,String content){
        return Notify.builder()
                .receiverEmail(receiverEmail)
                .senderEmail(senderEmail)
                .notificationType(notificationType)
                .content(content)
                .isRead(false)
                .build();
    }


}
