package semicolon.viewtist.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import semicolon.viewtis.chatting.dto.ChatMessageDto;
import semicolon.viewtis.chatting.form.ChatRoomForm;
import semicolon.viewtist.exception.ChattingException;
import semicolon.viewtist.exception.ErrorCode;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
  private final ObjectMapper mapper;
  private final Set<WebSocketSession> sessions = new HashSet<>();
  private final Map<String,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("{} 연결됨", session);
    sessions.add(session);
  }

  @Override
  protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
    String payload = message.getPayload();
    log.info("payload {}", payload);
    ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
    String streamKey = chatMessageDto.getStreamKey();
    if(!chatRoomSessionMap.containsKey(streamKey)){
      throw new ChattingException(ErrorCode.NOT_EXIST_STREAMKEY);
    }
    Set<WebSocketSession> chatRoomSession  = chatRoomSessionMap.get(chatMessageDto.getStreamKey());
    if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.ENTER)) {
      chatRoomSession.add(session);
    }
    sendMessageToChatRoom(chatMessageDto, chatRoomSession);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    // TODO Auto-generated method stub
    log.info("{} 연결 끊김", session.getId());
    sessions.remove(session);
  }
  public void createChatRoom(String streamKey) throws Exception {
    chatRoomSessionMap.put(streamKey,new HashSet<>());
    log.info(chatRoomSessionMap.toString());
    log.info(chatRoomSessionMap.entrySet().toString());
  }
  private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
    chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatMessageDto));
  }
  public <T> void sendMessage(WebSocketSession session, T message) {
    try{
      session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }
}