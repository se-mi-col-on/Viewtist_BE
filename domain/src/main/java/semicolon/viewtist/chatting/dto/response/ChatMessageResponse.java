package semicolon.viewtist.chatting.dto.response;

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
  private Long senderId;
  private String message;
  public static ChatMessageResponse from(ChatMessage chatMessage) {
    return ChatMessageResponse.builder()
        .senderId(chatMessage.getSenderId())
        .message(chatMessage.getMessage())
        .build();
  }
}
