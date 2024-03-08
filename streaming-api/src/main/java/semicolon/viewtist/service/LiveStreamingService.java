package semicolon.viewtist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingCreateRequest;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingUpdateRequest;
import semicolon.viewtist.liveStreaming.dto.response.LiveStreamingResponse;
import semicolon.viewtist.liveStreaming.entity.Category;
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

  public void startLiveStreaming(LiveStreamingCreateRequest liveStreamingCreateRequest,
      Authentication authentication) {

    User user = findByEmailOrThrow(authentication);
    LiveStreaming liveStreaming = liveStreamingCreateRequest.from(liveStreamingCreateRequest, user);
    liveStreamingRepository.save(liveStreaming);
  }

  public LiveStreamingResponse liveStreamingPage(Long StreamingId) {

    LiveStreaming liveStreaming = liveStreamingRepository.findById(StreamingId)
        .orElseThrow(() -> new LiveStreamingException(ErrorCode.LIVE_STREAMING_NOT_FOUND));

    return liveStreaming.form(liveStreaming);
  }

  public void updateLiveStreaming(
      LiveStreamingUpdateRequest liveStreamingUpdateRequest,
      Authentication authentication) {
    User user = findByEmailOrThrow(authentication);

    LiveStreaming liveStreaming = findLiveStreamingByUser(user);
    liveStreaming.update(liveStreamingUpdateRequest);
    liveStreamingRepository.save(liveStreaming);
  }

  public void stopLiveStreaming(Authentication authentication) {
    User user = findByEmailOrThrow(authentication);
    LiveStreaming liveStreaming = findLiveStreamingByUser(user);
    liveStreamingRepository.delete(liveStreaming);
  }

  public Page<LiveStreamingResponse> findLiveStreamings(Pageable pageable) {
    Page<LiveStreaming> liveStreamings = liveStreamingRepository.findAllByOrderByViewerCountDesc(
        pageable);
    return liveStreamings.map(LiveStreamingResponse::from);
  }

  public Page<LiveStreamingResponse> findLiveStreamingsByCategory(
      Category category, Pageable pageable) {
    Page<LiveStreaming> liveStreamings = liveStreamingRepository.findAllByCategory(category,
        pageable);
    return liveStreamings.map(LiveStreamingResponse::from);
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
