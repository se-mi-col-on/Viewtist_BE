package semicolon.viewtist.chatting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {

  // 메시지  타입 : 입장, 채팅, 후원
  public enum MessageType {
    ENTER, TALK,SUPPORT
  }
  private MessageType messageType; // 메시지 타입
  private Long streamingId; // 방 번호
  private String message; // 메시지
}