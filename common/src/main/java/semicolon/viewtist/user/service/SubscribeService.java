package semicolon.viewtist.user.service;

import static semicolon.viewtist.global.exception.ErrorCode.ALREADY_EXISTS_SUBSCRIBE;
import static semicolon.viewtist.global.exception.ErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.sse.entity.Subscribe;
import semicolon.viewtist.sse.repository.SubscribeRepository;
import semicolon.viewtist.user.dto.response.SubscribeResponse;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.SubscribeException;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SubscribeService {

  private final SubscribeRepository subscribeRepository;
  private final UserRepository userRepository;


  public void subscribe(String streamerNickname, Authentication authentication) {

    User streamer = getUserByNickname(streamerNickname);
    System.out.println(authentication.getName()+"  ===========");
    User user = getUserByEmail(authentication.getName());

    if (subscribeRepository.existsByReceiverAndUser(streamerNickname, user.getNickname())) {
      throw new SubscribeException(ALREADY_EXISTS_SUBSCRIBE);
    }

    Subscribe subscribe = Subscribe.builder()
        .user(user.getNickname())
        .receiver(streamer.getNickname())
        .build();

    subscribeRepository.save(subscribe);
  }

  @Transactional
  public void unsubscribe(String streamerNickname, Authentication authentication) {

    User streamer = getUserByNickname(streamerNickname);

    User user = getUserByEmail(authentication.getName());

    subscribeRepository.deleteByReceiverAndUser(streamer.getNickname(), user.getNickname());
  }

  public Page<SubscribeResponse> getSubscribeList(String userNickname,
      Pageable pageable) {
    User user = getUserByNickname(userNickname);
    Page<Subscribe> subscribes = subscribeRepository.findAllByUser(user.getNickname(), pageable);
    return subscribes.map(subscribe -> {
      User streamer = userRepository.findByNickname(subscribe.getReceiver()).orElseThrow(
          () -> new SubscribeException(USER_NOT_FOUND)
      );
      return SubscribeResponse.from(streamer.getNickname(), streamer.getProfilePhotoUrl());
    });
  }

  private User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UserException(ErrorCode.EMAIL_NOT_FOUND));
  }

  private User getUserByNickname(String nickname) {
    return userRepository.findByNickname(nickname)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
  }
}
