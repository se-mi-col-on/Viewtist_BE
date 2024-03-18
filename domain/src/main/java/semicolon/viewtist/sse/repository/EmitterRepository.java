package semicolon.viewtist.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
  SseEmitter save(String emitterId, SseEmitter sseEmitter);
  void saveEventCache(String emitterId, Object event);
  Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);
  Map<String, Object> findAllEventCacheStartWithByUserEmail(String memberId);
  void delete(String nickname);
  void deleteAllEmitterStartWithId(String memberId);
  void deleteAllEventCacheStartWithId(String memberId);

}