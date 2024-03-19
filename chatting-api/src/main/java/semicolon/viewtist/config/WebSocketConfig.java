package semicolon.viewtist.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import semicolon.viewtist.websocket.WebSocketChatHandler;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

  private final WebSocketChatHandler webSocketChatHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

    registry.addHandler(webSocketChatHandler, "/live/chat").setAllowedOrigins("*");
  }
}
