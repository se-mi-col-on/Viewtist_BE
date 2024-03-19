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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest;
import semicolon.viewtist.chatting.entity.ChatMessage;
import semicolon.viewtist.chatting.entity.ChatRoom;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.chatting.repository.ChatMessageRepository;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;
import semicolon.viewtist.liveStreaming.repository.LiveStreamingRepository;
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
  private final ChatRoomRepository chatRoomRepository;
  private final LiveStreamingRepository liveStreamingRepository;
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
    log.info("{} 연결 끊김", status);

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
@Transactional
  public void kickUser(String nickname, Long streamingId, Authentication authentication) throws Exception {
    User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
        () -> new UserException(ErrorCode.USER_NOT_FOUND)
    );
    LiveStreaming liveStreaming = liveStreamingRepository.findByUser(user).orElseThrow(
        () -> new ChattingException(ErrorCode.NOT_EXIST_STREAMKEY)
    );
    if(!user.getId().equals(liveStreaming.getChatRoom().getStreamerId())){
      throw new UserException(ErrorCode.ACCESS_DENIED);
    }
    User viewer = userRepository.findByNickname(nickname).orElseThrow(
        () -> new UserException(ErrorCode.USER_NOT_FOUND)
    );
    Set<WebSocketSession> chatRoomSession  = chatRoomSessionMap.get(streamingId);
    for(WebSocketSession webSocketSession: chatRoomSession){
     if(webSocketSession.getId().equals(viewer.getSessionId())){
       log.info(webSocketSession.getId()+viewer.getNickname());
       afterConnectionClosed(webSocketSession,new CloseStatus(1000,null));
     }
    }
  }

  public Integer getViewerCount(Long streamingId) {
    ChatRoom chatRoom = chatRoomRepository.findByStreamingId(streamingId).orElseThrow(
        () -> new ChattingException(ErrorCode.NOT_EXIST_STREAMKEY)
    );
    if(chatRoomSessionMap.get(streamingId)==null){
      return 0;
    }
    return chatRoomSessionMap.get(streamingId).size();
  }
}