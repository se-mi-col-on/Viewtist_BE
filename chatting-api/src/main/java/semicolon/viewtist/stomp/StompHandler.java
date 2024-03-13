package semicolon.viewtist.stomp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import semicolon.viewtist.chatting.entity.ChatRoom;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
  private final ChatRoomRepository chatRoomRepository;
  @Override
  public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    if (accessor.getCommand() == StompCommand.CONNECT) {
      // todo check jwt
      log.info("CONNECT ");
    }else if(accessor.getCommand() == StompCommand.SUBSCRIBE){
      log.info("SUBSCRIBE {} ", message.toString());
      String streamKey = extractDestination(
          (String) message.getHeaders().get("simpDestination"));

      if (streamKey == null) {
        throw new ChattingException(ErrorCode.NOT_EXIST_DESTINATION);
      }
      ChatRoom chatRoom = chatRoomRepository.findByStreamKey(streamKey).orElseThrow(
          () ->  new ChattingException(ErrorCode.NOT_EXIST_STREAMKEY)
      );
      if(!chatRoom.isActive()){
        throw new ChattingException(ErrorCode.DISABLED_CHAT_ROOM);
      }
      log.info(streamKey);

    }else if (StompCommand.DISCONNECT == accessor.getCommand()) {
//      String sessionId = (String) message.getHeaders().get("simpSessionId");
//      log.info("DISCONNECT {}",sessionId);
    }
    return message;
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