package semicolon.viewtist.handler;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.jwt.TokenProvider;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.repository.UserRepository;


@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler  implements ChannelInterceptor {
//  @Override
//  public Message<?> preSend(Message<?> message, MessageChannel channel) {
//    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//    if(accessor.getCommand() == StompCommand.CONNECT){
//      String token = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7);
//      log.info(token);
//      if(!tokenProvider.validateToken(token)){
//        throw new ChattingException(ErrorCode.INVALID_TOKEN);
//      }
//      User user = userRepository.findByEmail(tokenProvider.getAuthentication(token).getName()).orElseThrow(
//          () ->  new ChattingException(ErrorCode.USER_NOT_FOUND)
//      );
//      log.info("user connect {}",user.getNickname());
//    }
//    return message;
//  }
}
