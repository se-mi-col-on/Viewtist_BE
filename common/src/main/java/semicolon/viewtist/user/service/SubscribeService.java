package semicolon.viewtist.user.service;

import static semicolon.viewtist.global.exception.ErrorCode.ALREADY_EXISTS_SUBSCRIBE;

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


  public void unsubscribe(String streamerNickname, Authentication authentication) {

    User streamer = getUserByNickname(streamerNickname);

    User user = getUserByEmail(authentication.getName());

    subscribeRepository.deleteByReceiverAndUser(streamer.getNickname(), user.getNickname());
  }

  public Page<SubscribeResponse> getSubscribeList(Authentication authentication,
      Pageable pageable) {
    User user = getUserByEmail(authentication.getName());
    Page<Subscribe> subscribe = subscribeRepository.findAllByUser(user.getNickname(), pageable);
    return subscribe.map(SubscribeResponse::from);
  }

  private User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
  }

  private User getUserByNickname(String nickname) {
    return userRepository.findByNickname(nickname)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
  }
}
