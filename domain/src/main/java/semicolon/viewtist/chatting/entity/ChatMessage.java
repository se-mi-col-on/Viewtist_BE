package semicolon.viewtist.chatting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest.MessageType;
import semicolon.viewtist.global.entitiy.BaseTimeEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ChatMessage extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  private MessageType messageType; // 메시지 타입
  private Long streamingId; // 방 번호
  private String nickname; // 채팅을 보낸 사람
  private String message; // 메시지

  public static ChatMessage from(ChatMessageRequest request) {
    return ChatMessage.builder()
        .messageType(request.getMessageType())
        .streamingId(request.getStreamingId())
        .nickname(request.getNickname())
        .message(request.getMessage())
        .build();
  }
}