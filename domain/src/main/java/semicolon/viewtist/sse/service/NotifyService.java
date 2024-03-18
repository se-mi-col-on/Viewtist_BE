package semicolon.viewtist.sse.service;

import static semicolon.viewtist.sse.entity.Notify.NotificationType.STREAMING;
import static semicolon.viewtist.sse.entity.Notify.NotificationType.TEST;
import static semicolon.viewtist.sse.entity.Notify.NotificationType.VIEWTIST;

import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.sse.dto.response.NotifyStreamingResponse;
import semicolon.viewtist.sse.entity.Notify;
import semicolon.viewtist.sse.entity.Notify.NotificationType;
import semicolon.viewtist.sse.repository.EmitterRepositoryImpl;
import semicolon.viewtist.sse.repository.NotifyRepository;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifyService {

  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

  private final EmitterRepositoryImpl emitterRepository;
  private final NotifyRepository notifyRepository;
  private final UserRepository userRepository;

  public SseEmitter connect(Authentication authentication, String lastEventId) {
    User user = userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

    String emitterId = user.getNickname();

    SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
    emitter.onCompletion(() -> {
      log.info("Emitter completed. [emitterId={}]", emitterId);
      emitterRepository.delete(emitterId);
    });
    emitter.onTimeout(() -> {
      log.info("Emitter timeout. [emitterId={}]", emitterId);
      emitterRepository.delete(emitterId);
    });
    // 503 에러를 방지하기 위한 더미 이벤트 전송
    String eventId = makeTimeIncludeId(authentication.getName());
    sendNotification(emitter, eventId, emitterId,
        "EventStream Created. [userEmail=" + authentication.getName() + "]", TEST.toString());

    // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
    if (hasLostData(lastEventId)) {
      sendLostData(lastEventId, authentication.getName(), emitterId, emitter);
    }
    return emitter;
  }


  private String makeTimeIncludeId(String email) {
    return email + "_" + System.currentTimeMillis();
  }

  private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data,
      String notificationType) {
    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .name(notificationType)
          .data(data)
      );
    } catch (IOException exception) {
      emitterRepository.delete(emitterId);
    }
  }

  private boolean hasLostData(String lastEventId) {
    return !lastEventId.isEmpty();
  }

  private void sendLostData(String lastEventId, String userEmail, String emitterId,
      SseEmitter emitter) {
    Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserEmail(
        String.valueOf(userEmail));
    eventCaches.entrySet().stream()
        .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
        .forEach(entry ->
            sendNotification(emitter, entry.getKey(), emitterId, entry.getValue(),
                VIEWTIST.toString())
        );
  }

  public void streamingNotifySend(String receiver, NotificationType notificationType,
      String content) {

    String eventId = receiver + "_" + System.currentTimeMillis();

    NotifyStreamingResponse response = NotifyStreamingResponse.builder()
        .receiverName(receiver)
        .notifyId(eventId)
        .content(content)
        .type(STREAMING.toString())
        .build();

    User receiverUser = userRepository.findByNickname(receiver)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

    notifyRepository.save(
        createNotification(receiverUser, notificationType, response.getContent()));

    emitterRepository.get(receiver).ifPresentOrElse(sseEmitter -> {
      try {
        sseEmitter.send(
            SseEmitter.event().id(eventId).name(String.valueOf(STREAMING))
                .data(response));
      } catch (IOException e) {
        emitterRepository.delete(receiver);
      }
    }, () -> log.info("[SseEmitter] {} SseEmitter Not Founded", receiver));
  }

  private Notify createNotification(User receiver, NotificationType notificationType,
      String content) {
    return Notify.builder()
        .receiver(receiver)
        .notificationType(notificationType)
        .content(content)
        .isRead(false)
        .build();
  }

}