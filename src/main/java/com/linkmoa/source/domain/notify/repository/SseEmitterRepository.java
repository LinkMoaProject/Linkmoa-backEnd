package com.linkmoa.source.domain.notify.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface SseEmitterRepository {

    SseEmitter save(String emitterId,SseEmitter sseEmitter);
    void saveEventCache(String emitterId,Object event);
    Map<String,SseEmitter> findAllEmitterStartWithByMemberId(String memberId);
    Map<String,Object> findAllEventCacheStartWithByMemberId(String memberId);
    void deleteById(String id);
    void deleteAllEmitterStartWithId(String memberId);
    void deleteAllEventCacheStartWtihId(String memberId);
}
