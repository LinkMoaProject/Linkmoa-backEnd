package com.linkmoa.source.domain.notification.repository;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class SseEmitterRepositoryImpl implements SseEmitterRepository {

	/**
	 * emitter는 클라이언트가 구독을 요청하면 해당 사용자의 삭별자를 사용하여 맵에 저장, 이후 알림을 전송할 때 해당 사용자의
	 * SseEmitter를 조회하기 위해 사용함
	 */
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	/**
	 * eventCahce는 알림을 받을 사용자의 식별자를 키로 저장하고,
	 * 해당 사용자에게 전송되지 못한 이벤트를 캐시로 저장하는 역할
	 * 저장된 이벤트는 사용자가 구독할 때, 클라이언트로 전송되어 이벤트의 유실을 방지함
	 */

	private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

	/**
	 *  SSE 이벤트 전송 객체(emitter)를 저장
	 * @param emitterId
	 * @param sseEmitter
	 * @return
	 */
	@Override
	public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
		emitters.put(emitterId, sseEmitter);

		return sseEmitter;
	}

	/**
	 * 이벤트 객체를 저장
	 * @param eventCacheId
	 * @param event
	 */

	@Override
	public void saveEventCache(String eventCacheId, Object event) {
		eventCache.put(eventCacheId, event);
	}

	/**
	 * 해당 회원과 관련된 모든 Emitter를 조회
	 * @param memberId
	 * @return
	 */
	@Override
	public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
		return emitters.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(memberId))
			.collect(Collectors.toMap(
				Map.Entry::getKey // entry -> entry.getKey()
				, Map.Entry::getValue // entry -> entry.getValue()
			));
	}

	/**
	 * 해당 회원과 관련된 모든 event를 조회
	 * @param memberId
	 * @return
	 */

	@Override
	public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
		return eventCache.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(memberId))
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				Map.Entry::getValue
			));
	}

	@Override
	public void deleteById(String id) {
		emitters.remove(id);

	}

	/**
	 * 해당 회원과 관련된 모든 Emitter를 삭제
	 * @param memberId
	 */
	@Override
	public void deleteAllEmitterStartWithId(String memberId) {
		Iterator<Map.Entry<String, SseEmitter>> iterator = emitters.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, SseEmitter> entry = iterator.next();
			if (entry.getKey().startsWith(memberId)) {
				iterator.remove();
			}
		}

/*        emitters.forEach(
                (key,emitter) ->{
                    if(key.startsWith(memberId)){
                        emitters.remove(key);
                    }
                }
        );*/

	}

	/**
	 * 해당 회원과 관련된 모든 event를 삭제
	 * @param memberId
	 */
	@Override
	public void deleteAllEventCacheStartWtihId(String memberId) {

		Iterator<Map.Entry<String, Object>> iterator = eventCache.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();
			if (entry.getKey().startsWith(memberId)) {
				iterator.remove();
			}
		}
/*        eventCache.forEach(
                (key,emitters) ->{
                    if(key.startsWith(memberId)){
                        eventCache.remove(key);
                    }
                });*/

	}
}
