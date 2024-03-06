package semicolon.viewtist.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import semicolon.viewtis.chatting.dto.ChatRoomDto;
import semicolon.viewtis.chatting.entity.ChatRoom;
import semicolon.viewtis.chatting.form.ChatRoomForm;
import semicolon.viewtis.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.exception.ChattingException;
import semicolon.viewtist.exception.ErrorCode;
import semicolon.viewtist.websocket.WebSocketChatHandler;

@Slf4j
@RequiredArgsConstructor
@Service
public class LiveChatService {
  private final WebSocketChatHandler webSocketChatHandler;
  private final ChatRoomRepository chatRoomRepository;
  public String createChatroom(ChatRoomForm chatRoomForm) throws Exception {
    if(chatRoomRepository.findByStreamKey(chatRoomForm.getStreamKey()).isPresent()){
      throw new ChattingException(ErrorCode.ALREADY_EXIST_STREAMKEY);
    }
    chatRoomRepository.save(ChatRoom.from(chatRoomForm));
    webSocketChatHandler.createChatRoom(chatRoomForm.getStreamKey());
    return  chatRoomForm.getStudioName();
  }
  public List<ChatRoomDto> getAllChatroom() {
    return chatRoomRepository.findAll().stream()
        .map(ChatRoomDto::from)
        .collect(Collectors.toList());
  }

}
