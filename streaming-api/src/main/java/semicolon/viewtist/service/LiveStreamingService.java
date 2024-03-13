package semicolon.viewtist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingCreateRequest;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingUpdateRequest;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;
import semicolon.viewtist.liveStreaming.exception.LiveStreamingException;
import semicolon.viewtist.liveStreaming.repository.LiveStreamingRepository;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LiveStreamingService {

  private final LiveStreamingRepository liveStreamingRepository;
  private final UserRepository userRepository;

  // 스트리밍 시작
  public void startLiveStreaming(LiveStreamingCreateRequest liveStreamingCreateRequest,
      Authentication authentication) {

    User user = findByEmailOrThrow(authentication);
    LiveStreaming liveStreaming = liveStreamingCreateRequest.from(liveStreamingCreateRequest, user);
    liveStreamingRepository.save(liveStreaming);
  }

  // 스트리밍 업데이트
  public void updateLiveStreaming(
      LiveStreamingUpdateRequest liveStreamingUpdateRequest,
      Authentication authentication) {
    User user = findByEmailOrThrow(authentication);

    LiveStreaming liveStreaming = findLiveStreamingByUser(user);
    liveStreaming.update(liveStreamingUpdateRequest);
    liveStreamingRepository.save(liveStreaming);
  }


  private LiveStreaming findLiveStreamingByUser(User user) {
    return liveStreamingRepository.findByUser(user)
        .orElseThrow(() -> new LiveStreamingException(ErrorCode.LIVE_STREAMING_NOT_FOUND));
  }

  private User findByEmailOrThrow(Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
  }

}
