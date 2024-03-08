package semicolon.viewtist.service;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest.MessageType;
import semicolon.viewtist.chatting.dto.request.ChatRoomRequest;
import semicolon.viewtist.chatting.dto.response.ChatRoomResponse;
import semicolon.viewtist.chatting.entity.ChatRoom;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
  private final SimpMessageSendingOperations sendingOperations;
  private final ChatRoomRepository chatRoomRepository;
  private Map<String, ChatRoom> chatRooms;
  @PostConstruct
  private void init() {
    chatRooms = new LinkedHashMap<>();
  }

  public List<ChatRoomResponse> findAllRoom() {
    List<ChatRoomResponse> result = chatRooms.values()
        .stream().map(ChatRoomResponse::from)
        .collect(Collectors.toList());
    Collections.reverse(result);
    return result;
  }

  //채팅방 생성
  public void createRoom(ChatRoomRequest request) {
    if(chatRoomRepository.findByStreamKey(request.getStreamKey()).isPresent()){
      throw new ChattingException(ErrorCode.ALREADY_EXIST_STREAMKEY);
    }
    chatRooms.put(request.getStreamKey(), ChatRoom.from(request));
  }

  public ChatRoomResponse enterChatRoom(String streamKey) {
     return ChatRoomResponse.from(chatRooms.get(streamKey));
  }

  public String sendChatMessage(ChatMessageRequest message) {
    if (MessageType.ENTER.equals(message.getMessageType())) {
      message.setMessage(message.getSenderId()+"님이 입장하였습니다.");
    }
    // 헤당 경로를 구독하는 사람에게 메세지 전달
    sendingOperations.convertAndSend("/topic/chatroom/"+message.getStreamKey(),message);
    return message.getMessage();
  }
}
