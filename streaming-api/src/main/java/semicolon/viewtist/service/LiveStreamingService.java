package semicolon.viewtist.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.viewtist.chatting.entity.ChatRoom;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingCreateRequest;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingUpdateRequest;
import semicolon.viewtist.liveStreaming.dto.response.LiveStreamingResponse;
import semicolon.viewtist.liveStreaming.entity.Category;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;
import semicolon.viewtist.liveStreaming.exception.LiveStreamingException;
import semicolon.viewtist.liveStreaming.repository.LiveStreamingRepository;
import semicolon.viewtist.sse.entity.Notify.NotificationType;
import semicolon.viewtist.sse.entity.Subscribe;
import semicolon.viewtist.sse.repository.SubscribeRepository;
import semicolon.viewtist.sse.service.NotifyService;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveStreamingService {
  private final LiveStreamingRepository liveStreamingRepository;
  private final UserRepository userRepository;
  private final NotifyService notifyService;
  private final SubscribeRepository subscribeRepository;
  private final ChatRoomRepository chatRoomRepository;

  // 스트리밍 시작
  @Transactional
  public LiveStreamingResponse startLiveStreaming(
      LiveStreamingCreateRequest liveStreamingCreateRequest,
      Authentication authentication) {
    User user = findByEmail(authentication);
    Optional<LiveStreaming> optionalLiveStreaming = liveStreamingRepository.findByUser(user);
    if (optionalLiveStreaming.isPresent()) {
      throw new LiveStreamingException(ErrorCode.ALREADY_LIVE_STREAMING);
    }
    // 스트리밍 생성
    LiveStreaming liveStreaming =
      liveStreamingRepository.save(liveStreamingCreateRequest.from(liveStreamingCreateRequest, user));
    // 채팅방 생성
   createChatRoom(user,liveStreaming);
    // 알림전송
    sendAlarmToSubscriber(user);
    return LiveStreamingResponse.from(liveStreaming);
  }

  private void createChatRoom(User user, LiveStreaming liveStreaming){
    if(chatRoomRepository.existsByStreamerId(user.getId())){
      throw new ChattingException(ErrorCode.ALREADY_CREATE_ANOTHER_ROOM);
    }
    ChatRoom chatRoom =
        chatRoomRepository.save(ChatRoom.madeByUser(user));
    liveStreaming.createChatRoom(chatRoom);
  }
  private void sendAlarmToSubscriber(User user){
    List<Subscribe> subscribeList = subscribeRepository.findByReceiver(user.getNickname());
    for (Subscribe subscribe : subscribeList) {
      notifyService.streamingNotifySend(subscribe.getUser(), NotificationType.STREAMING,
          user.getNickname() + " is start stremaing"
      );
      log.info(subscribe.getUser() + "에게 알림을 보냈습니다.");
    }
  }

  // 스트리밍 업데이트
  public void updateLiveStreaming(Long streamId,
      LiveStreamingUpdateRequest liveStreamingUpdateRequest, Authentication authentication) {

    LiveStreaming liveStreaming = liveStreamingFindById(streamId);

    if (!liveStreaming.getUser().getEmail().equals(authentication.getName())) {
      throw new UserException(ErrorCode.USER_NOT_MATCH);
    }
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

  // 스트리밍 삭제
  public void stopStreaming(Long streamId, Authentication authentication) {
    LiveStreaming liveStreaming = liveStreamingFindById(streamId);
    if (!liveStreaming.getUser().getEmail().equals(authentication.getName())) {
      throw new UserException(ErrorCode.USER_NOT_MATCH);
    }
    liveStreamingRepository.delete(liveStreaming);
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
