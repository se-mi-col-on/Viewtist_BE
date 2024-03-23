package semicolon.viewtist.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest.MessageType;
import semicolon.viewtist.chatting.dto.response.ChatMessageResponse;
import semicolon.viewtist.chatting.entity.ChatMessage;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.chatting.repository.ChatMessageRepository;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final ChatMessageRepository chatMessageRepository;
  private final UserRepository userRepository;
  private final SimpMessageSendingOperations operations;
  private final String WELCOME = " 님이 들어오셨습니다.";
  private final String GOODBYE = " 님이 나가셨습니다.";
  // 이전 채팅 메세지 내역
  public List<ChatMessageResponse> getChatMessages(Long streamingId){
    return chatMessageRepository.findByStreamingIdOrderByCreatedAtDesc(streamingId)
        .stream().map(ChatMessageResponse::from)
        .collect(Collectors.toList());
  }

  public void sendMessage(ChatMessageRequest chatMessageRequest, Authentication authentication) {
    User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
        () -> new ChattingException(ErrorCode.USER_NOT_FOUND)
    );
    if(MessageType.ENTER.equals(chatMessageRequest.getMessageType())){
      chatMessageRequest.setMessage(user.getNickname()+WELCOME);
    }
    ChatMessage chatMessage = ChatMessage.from(chatMessageRequest);
    chatMessage.setNickname(user.getNickname());
    operations.convertAndSend
        ("/sub/room/" + chatMessage.getStreamingId(), chatMessage.getMessage());
  }
}
