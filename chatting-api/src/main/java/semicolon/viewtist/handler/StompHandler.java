package semicolon.viewtist.handler;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;
import semicolon.viewtist.liveStreaming.exception.LiveStreamingException;
import semicolon.viewtist.liveStreaming.repository.LiveStreamingRepository;
import semicolon.viewtist.service.ChatRoomService;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler  implements ChannelInterceptor {
  private final LiveStreamingRepository liveStreamingRepository;
  private final ChatRoomService chatRoomService;
  private  HashMap<String,Long> sessionStreamMap;
  @PostConstruct
  private void init(){
    sessionStreamMap = new HashMap<>();
  }

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    String sessionId = accessor.getSessionId();
    if(accessor.getCommand() == StompCommand.CONNECT){
      sessionStreamMap.put(sessionId,null);
    }else if(accessor.getCommand() == StompCommand.SUBSCRIBE){
      if(accessor.getHeader("simpDestination") == null){
        throw new ChattingException(ErrorCode.NOT_EXIST_DESTINATION);
      }
      String destination = (String) accessor.getHeader("simpDestination");
      Long streamingId = Long.parseLong(Objects.requireNonNull(extractDestination(destination)));

      sessionStreamMap = chatRoomService.enter(sessionId,streamingId,sessionStreamMap);
    }else{
      sessionStreamMap = chatRoomService.exit(sessionId,sessionStreamMap);
      log.info("연결종료");
    }
    return message;
  }
  @Transactional
  private void removeSessionMap(String sessionId){
    sessionStreamMap.remove(sessionId);
  }

  private static String extractDestination(String input) {
    String pattern = "/(\\w+)$"; // 정규표현식 패턴
    Pattern r = Pattern.compile(pattern);
    Matcher m = r.matcher(input);

    if (m.find()) {
      return m.group(1); // 매칭된 부분 반환
    } else {
      return null; // 매칭된 부분이 없을 경우 null 반환 또는 적절한 오류 처리
    }
  }
}
