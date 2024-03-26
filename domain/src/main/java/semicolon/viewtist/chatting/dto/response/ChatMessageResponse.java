package semicolon.viewtist.chatting.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.viewtist.chatting.entity.ChatMessage;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
  private String nickname;
  private String message;
  private Long streamingId;
  private LocalDateTime createdAt;

  public static ChatMessageResponse from(ChatMessage chatMessage) {
    return ChatMessageResponse.builder()
        .nickname(chatMessage.getNickname())
        .message(chatMessage.getMessage())
        .streamingId(chatMessage.getStreamingId())
        .createdAt(chatMessage.getCreatedAt())
        .build();
  }
}
