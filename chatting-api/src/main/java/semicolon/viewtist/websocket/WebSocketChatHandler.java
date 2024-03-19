package semicolon.viewtist.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.service.ChatMessageService;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;


@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
  private final ObjectMapper mapper;
  private final Set<WebSocketSession> sessions = new HashSet<>();
  private final Map<String,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();
  private UserRepository userRepository;
  private ChatMessageService chatMessageService;

// 스트리밍을 시청할때 채팅방 접속
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("{} 연결됨", session);
    sessions.add(session);
  }
  @Override
  protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
    String payload = message.getPayload();
    log.info("payload {}", payload);
    ChatMessageRequest chatMessageRequest = mapper.readValue(payload, ChatMessageRequest.class);
    String streamKey = chatMessageRequest.getStreamKey();
    if(!chatRoomSessionMap.containsKey(streamKey)){
      chatRoomSessionMap.put(streamKey,new HashSet<>());
    }
    Set<WebSocketSession> chatRoomSession  = chatRoomSessionMap.get(chatMessageRequest.getStreamKey());
    if (chatMessageRequest.getMessageType().equals(ChatMessageRequest.MessageType.ENTER)) {
      User user = userRepository.findById(chatMessageRequest.getSenderId()).orElseThrow(
          () -> new UserException(ErrorCode.USER_NOT_FOUND)
      );
      user.setSessionId(session.getId());
      chatRoomSession.add(session);
    }
    sendMessageToChatRoom(chatMessageRequest, chatRoomSession);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    // TODO Auto-generated method stub
    log.info("{} 연결 끊김", session.getId());
    sessions.remove(session);
  }

  private void sendMessageToChatRoom(ChatMessageRequest chatMessage, Set<WebSocketSession> chatRoomSession) {
    chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatMessage));
  }
  public <T> void sendMessage(WebSocketSession session, T message) {
    try{
      session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }
}