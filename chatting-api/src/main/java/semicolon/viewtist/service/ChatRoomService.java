package semicolon.viewtist.service;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.viewtist.chatting.entity.ChatRoom;
import semicolon.viewtist.chatting.entity.type.Status;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;
import semicolon.viewtist.liveStreaming.exception.LiveStreamingException;
import semicolon.viewtist.liveStreaming.repository.LiveStreamingRepository;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;


@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
  private final LiveStreamingRepository liveStreamingRepository;
@Transactional
  public HashMap<String,Long> enter(String sessionId, Long streamingId, HashMap<String,Long> sessionMap) {
    LiveStreaming streaming = liveStreamingRepository.findById(streamingId).orElseThrow(
        () -> new LiveStreamingException(ErrorCode.NOT_EXIST_STREAMINGID)
    );
    streaming.addViewCount();
    sessionMap.put(sessionId,streamingId);
    return sessionMap;
  }
  @Transactional
  public HashMap<String, Long> exit(String sessionId, HashMap<String,Long> sessionMap){
  Long streamingId = sessionMap.get(sessionId);
  if(streamingId != null){
    LiveStreaming streaming = liveStreamingRepository.findById(streamingId).orElseThrow(
        () -> new LiveStreamingException(ErrorCode.NOT_EXIST_STREAMINGID)
    );
    streaming.minusViewCount();
    sessionMap.remove(sessionId);
  }

    return sessionMap;
  }
  public Long getViews(Long streamingId){
    LiveStreaming streaming = liveStreamingRepository.findById(streamingId).orElseThrow(
        () -> new LiveStreamingException(ErrorCode.NOT_EXIST_STREAMINGID)
    );
    return streaming.getViewerCount();
  }

}
