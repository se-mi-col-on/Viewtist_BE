package semicolon.viewtist.service;

import java.util.Optional;
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

  // 스트리밍 시작
  public LiveStreamingResponse startLiveStreaming(LiveStreamingCreateRequest liveStreamingCreateRequest,
      Authentication authentication) {
    User user = findByEmail(authentication);
    Optional<LiveStreaming> optionalLiveStreaming = liveStreamingRepository.findByUser(user);
    if (optionalLiveStreaming.isPresent()) {
      throw new LiveStreamingException(ErrorCode.ALREADY_LIVE_STREAMING);
    }

    LiveStreaming liveStreaming = liveStreamingCreateRequest.from(liveStreamingCreateRequest, user);
    liveStreamingRepository.save(liveStreaming);
    return LiveStreamingResponse.from(liveStreaming);
  }

  // 스트리밍 업데이트
  public void updateLiveStreaming(Long streamId,
      LiveStreamingUpdateRequest liveStreamingUpdateRequest) {

    LiveStreaming liveStreaming = liveStreamingFindById(streamId);

    liveStreaming.update(liveStreamingUpdateRequest);
    liveStreamingRepository.save(liveStreaming);
  }

  public LiveStreamingResponse getLiveStreaming(Long streamId) {
    LiveStreaming liveStreaming = liveStreamingFindById(streamId);
    return LiveStreamingResponse.from(liveStreaming);
  }

  // 스트리밍 전체 조회
  public Page<LiveStreamingResponse> findLiveStreamings(Pageable pageable) {
    Page<LiveStreaming> liveStreamings = liveStreamingRepository.findAll(
        pageable);
    return liveStreamings.map(LiveStreamingResponse::from);
  }

  // 스트리밍 카테고리별로 분류
  public Page<LiveStreamingResponse> findLiveStreamingsByCategory(
      Category category, Pageable pageable) {
    Page<LiveStreaming> liveStreamings = liveStreamingRepository.findAllByCategory(category,
        pageable);
    return liveStreamings.map(LiveStreamingResponse::from);
  }


  private LiveStreaming liveStreamingFindById(Long streamId) {
    return liveStreamingRepository.findById(streamId)
        .orElseThrow(() -> new LiveStreamingException(ErrorCode.LIVE_STREAMING_NOT_FOUND));
  }


  private User findByEmail(Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
  }

}
