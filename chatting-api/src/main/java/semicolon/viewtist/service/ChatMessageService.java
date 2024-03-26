package semicolon.viewtist.service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest.MessageType;
import semicolon.viewtist.chatting.dto.response.ChatMessageResponse;
import semicolon.viewtist.chatting.entity.ChatMessage;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.chatting.repository.ChatMessageRepository;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.jwt.TokenProvider;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;
import semicolon.viewtist.liveStreaming.exception.LiveStreamingException;
import semicolon.viewtist.liveStreaming.repository.LiveStreamingRepository;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;
  private final LiveStreamingRepository liveStreamingRepository;


  private final String WELCOME = " 님이 들어오셨습니다.";
  private final String GOODBYE = " 님이 나가셨습니다.";
  // 이전 채팅 메세지 내역
  public Page<ChatMessageResponse> getChatMessages(Long streamingId, Pageable pageable){
    Page<ChatMessage> chatMessages = chatMessageRepository.findByStreamingIdOrderByCreatedAtDesc(streamingId, pageable);
   return chatMessages.map(ChatMessageResponse::from);
  }
@Transactional
  public ChatMessageResponse manageChatMessage(ChatMessageRequest chatMessageRequest) {
    User user = userRepository.findByNickname(chatMessageRequest.getNickname()).orElseThrow(
        () -> new ChattingException(ErrorCode.USER_NOT_FOUND)
    );
    chatRoomRepository.findByStreamingId(chatMessageRequest.getStreamingId()).orElseThrow(
        () -> new ChattingException(ErrorCode.NOT_EXIST_STREAMINGID)
    );
    ChatMessage chatMessage = ChatMessage.from(chatMessageRequest);
    chatMessageRepository.save(chatMessage);
    return ChatMessageResponse.from(chatMessage);
  }
}
