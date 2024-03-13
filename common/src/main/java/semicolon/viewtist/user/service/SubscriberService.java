package semicolon.viewtist.user.service;

import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import semicolon.viewtist.alarm.EmitterRepository;
import semicolon.viewtist.global.exception.CustomException;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.user.entity.Subscribe;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.SubscribeRepository;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SubscriberService {
  private final SubscribeRepository subscribeRepository;
  private final UserRepository userRepository;
  public void subscribeStreamer(Long streamerId, Authentication authentication) {
    User streamer = userRepository.findById(streamerId).orElseThrow(
        ()-> new UserException(ErrorCode.USER_NOT_FOUND)
    );
    User viewer = userRepository.findByEmail(authentication.getName()).orElseThrow(
        ()-> new UserException(ErrorCode.EMAIL_NOT_FOUND)
    );
    subscribeRepository.save(
        Subscribe.builder()
            .streamerId(streamer.getId())
            .userId(viewer.getId())
            .build());
  }

  private SseEmitter connectAlarm(String nickname){
    SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitterRepository.save(username, sseEmitter);

    // 종료 되었을 때 처리
    sseEmitter.onCompletion(() -> {
      emitterRepository.delete(username);
    });

    // timeOut 시 처리
    sseEmitter.onTimeout(() -> {
      emitterRepository.delete(username);
    });

    try {
      sseEmitter.send(SseEmitter.event().id(createAlarmId(username)).name(ALARM_NAME)
          .data("connect completed!!"));
    } catch (IOException e) {
      throw new CustomException(ErrorCode.SERVER_DISCONNECT);
    }

    return sseEmitter;
  }
  public void cancelSubscribe(Long subscribeId, Authentication authentication) {
    Subscribe subscribe = subscribeRepository.findById(subscribeId).orElseThrow(
        () -> new UserException(ErrorCode.SUBSCRIBE_NOT_FOUND)
    );
    User viewer = userRepository.findByEmail(authentication.getName()).orElseThrow(
        () -> new UserException(ErrorCode.EMAIL_NOT_FOUND)
    );
    if (!Objects.equals(viewer.getId(), subscribe.getUserId())) {
      throw new UserException(ErrorCode.UNVALID_SUBSCRIBE_CANCEL);
    }
    subscribeRepository.delete(subscribe);
  }
}
