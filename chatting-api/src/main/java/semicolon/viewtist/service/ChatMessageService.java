package semicolon.viewtist.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest.MessageType;
import semicolon.viewtist.chatting.dto.response.ChatMessageResponse;
import semicolon.viewtist.chatting.entity.ChatMessage;
import semicolon.viewtist.chatting.repository.ChatMessageRepository;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.repository.UserRepository;
import semicolon.viewtist.websocket.WebSocketChatHandler;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private UserRepository userRepository;


  // 이전 채팅 메세지 내역
  public List<ChatMessageResponse> getChatMessages(String streamKey){
    return chatMessageRepository.findByStreamKeyOrderByCreatedAtDesc(streamKey)
        .stream().map(ChatMessageResponse::from)
        .collect(Collectors.toList());
  }
}
