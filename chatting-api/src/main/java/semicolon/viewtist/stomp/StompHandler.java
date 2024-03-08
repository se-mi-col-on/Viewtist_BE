package semicolon.viewtist.stomp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
  @Override
  public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
    return message;
  }
}