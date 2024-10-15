package com.linkmoa.source.domain.notify.service;


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




    /*public SseEmitter subscribe(String email,String lastEventId){
        String emitterId = makeTimeIncludeId(email);

        SseEmitter newEmitter = sseEmitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // newEmitter의 콜백 메소드
        newEmitter.onCompletion(()-> sseEmitterRepository.deleteById(emitterId));
        newEmitter.onTimeout(() -> sseEmitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(email);
        sendNotification(newEmitter,eventId,emitterId,"연결 완료됐습니다. [user Email = "+email+"]");

        if(hasLostData(lastEventId)){
            sendLostData.
        }


    }*/

    private void sendNotification(SseEmitter emitter,String eventId, String emitterId,Object data){
        try{
           emitter.send(
                    SseEmitter.event()
                            .id(eventId)
                            .name("SSE")
                            .data(data)
            );
        }catch (IOException e){
            sseEmitterRepository.deleteById(emitterId);
        }
    }


    private String makeTimeIncludeId(String email){
        return email + "_"+System.currentTimeMillis();
    }

    private boolean hasLostData(String lastEventId){
        return !lastEventId.isEmpty();
    }

  /*  private void sendLostData(String lastEventId,String email,SseEmitter emitter){
        Map<String,Object> eventCaches = sseEmitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(emitter));
        eventCaches.entrySet().stream()
                .filter()
    }*/


}
