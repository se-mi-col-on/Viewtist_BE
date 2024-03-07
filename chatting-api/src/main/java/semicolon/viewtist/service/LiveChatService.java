package semicolon.viewtist.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import semicolon.viewtist.chatting.dto.response.ChatRoomResponse;
import semicolon.viewtist.chatting.entity.ChatRoom;
import semicolon.viewtist.chatting.dto.request.ChatRoomRequest;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.websocket.WebSocketChatHandler;

@Slf4j
@RequiredArgsConstructor
@Service
public class LiveChatService {
  private final WebSocketChatHandler webSocketChatHandler;
  private final ChatRoomRepository chatRoomRepository;
  public String createChatroom(ChatRoomRequest chatRoomRequest) throws Exception {
    if(chatRoomRepository.findByStreamKey(chatRoomRequest.getStreamKey()).isPresent()){
      throw new ChattingException(ErrorCode.ALREADY_EXIST_STREAMKEY);
    }
    chatRoomRepository.save(ChatRoom.from(chatRoomRequest));
    webSocketChatHandler.createChatRoom(chatRoomRequest.getStreamKey());
    return  chatRoomRequest.getStudioName();
  }
  public List<ChatRoomResponse> getAllChatroom() {
    return chatRoomRepository.findAll().stream()
        .map(ChatRoomResponse::from)
        .collect(Collectors.toList());
  }

}
