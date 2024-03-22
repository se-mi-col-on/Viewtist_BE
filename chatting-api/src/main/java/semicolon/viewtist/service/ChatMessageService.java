package semicolon.viewtist.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import semicolon.viewtist.chatting.dto.response.ChatMessageResponse;
import semicolon.viewtist.chatting.repository.ChatMessageRepository;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final ChatMessageRepository chatMessageRepository;

  // 이전 채팅 메세지 내역
  public List<ChatMessageResponse> getChatMessages(Long streamingId){
    return chatMessageRepository.findByStreamingIdOrderByCreatedAtDesc(streamingId)
        .stream().map(ChatMessageResponse::from)
        .collect(Collectors.toList());
  }
}
