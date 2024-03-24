package semicolon.viewtist.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.jwt.TokenProvider;
import semicolon.viewtist.service.ChatMessageService;



@RequiredArgsConstructor
@Component
@Slf4j
@Order(99)
public class StompHandler  implements ChannelInterceptor {
  private final ChatMessageService chatMessageService;
  private final TokenProvider tokenProvider;
  private final HashSet<String> sessions;
  private final HashMap<Long, Set<String>> streamingSessionMap;
  private final HashMap<String,String> userSessionMap;
  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    String sessionId = accessor.getSessionId();
//    if(accessor.getCommand() == StompCommand.CONNECT){
//      log.info("connect {}",sessionId);
//
//    }else if(accessor.getCommand() == StompCommand.SUBSCRIBE){
//      if(accessor.getHeader("simpDestination") == null){
//        throw new ChattingException(ErrorCode.NOT_EXIST_DESTINATION);
//      }
//      String destination = (String) accessor.getHeader("simpDestination");
//      Long streamingId = Long.parseLong(Objects.requireNonNull(extractDestination(destination)));
//      log.info("streamingId {}",streamingId);
//      log.info("subscribe {}",sessionId);
//    }
    return message;
  }
//  private static String extractDestination(String input) {
//    String pattern = "/(\\w+)$"; // 정규표현식 패턴
//    Pattern r = Pattern.compile(pattern);
//    Matcher m = r.matcher(input);
//
//    if (m.find()) {
//      return m.group(1); // 매칭된 부분 반환
//    } else {
//      return null; // 매칭된 부분이 없을 경우 null 반환 또는 적절한 오류 처리
//    }
//  }
}
