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
import semicolon.viewtist.chatting.entity.ChatMessage;
import semicolon.viewtist.chatting.repository.ChatMessageRepository;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.service.ChatRoomService;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;


@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
  private final ObjectMapper mapper;
  private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();
  private final Map<WebSocketSession, Long> sessionChatRoomMap = new HashMap<>();
  private final UserRepository userRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomService chatRoomService;
  private final String ENTER=" 님이 입장하였습니다.";
  private final String EXIT=" 님이 퇴장하였습니다.";
// 스트리밍을 시청할때 채팅방 접속
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("{} 연결됨", session);
    sessionChatRoomMap.put(session,null);
  }
  @Override
  protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
    String payload = message.getPayload();
    log.info("payload {}", payload);
    ChatMessageRequest chatMessageRequest = mapper.readValue(payload, ChatMessageRequest.class);
    Long streamingId= chatMessageRequest.getStreamingId();
    if(!chatRoomSessionMap.containsKey(streamingId)){
      chatRoomSessionMap.put(streamingId,new HashSet<>());
    }
    Set<WebSocketSession> chatRoomSession  = chatRoomSessionMap.get(chatMessageRequest.getStreamingId());
    if (chatMessageRequest.getMessageType().equals(ChatMessageRequest.MessageType.ENTER)) {
      User user = chatRoomService.setUserSessionId(chatMessageRequest.getSenderId(), session.getId());
      chatMessageRequest.setMessage(user.getNickname()+ENTER);
      chatRoomSession.add(session);
      sessionChatRoomMap.put(session,streamingId);
    }
    chatMessageRepository.save(ChatMessage.from(chatMessageRequest));
    sendMessageToChatRoom(chatMessageRequest, chatRoomSession);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    // TODO Auto-generated method stub
    log.info("{} 연결 끊김", session.getId());

    User user = userRepository.findBySessionId(session.getId()).orElseThrow(
        () -> new UserException(ErrorCode.USER_NOT_FOUND)
    );
    chatRoomService.removeUserSessionId(user);
    Long streamingId = sessionChatRoomMap.get(session);
    Set<WebSocketSession> chatRoomSession  = chatRoomSessionMap.get(streamingId);
    chatRoomSessionMap.get(streamingId).remove(session);
    sessionChatRoomMap.remove(session);
    sendMessageToChatRoom(ChatMessageRequest.builder().message(user.getNickname()+EXIT).build(),chatRoomSession);
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