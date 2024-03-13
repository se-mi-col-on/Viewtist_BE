package semicolon.viewtist.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import semicolon.viewtist.stomp.StompHandler;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompConfig implements WebSocketMessageBrokerConfigurer { // message broker 설정

  private final StompHandler stompHandler;
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // websocket 실행 경로
    registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*");
    // .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) { // 스프링에 내장되어있는 메세지 브로커 사용
    // 1:1 queue
    // 1:N topic
    registry.enableSimpleBroker("/queue", "/topic");
    // prefix : app 으로 시작하는 경로에서 발행됨.
    registry.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(stompHandler);
  }
}
