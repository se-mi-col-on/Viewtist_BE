package semicolon.viewtis.chatting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import semicolon.viewtis.chatting.dto.ChatMessageDto.MessageType;
import semicolon.viewtis.core.global.BaseTimeEntity;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@AuditOverride(forClass = BaseTimeEntity.class)
public class ChatMessage extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private MessageType messageType; // 메시지 타입
  private String streamKey; // 방 번호
  private Long senderId; // 채팅을 보낸 사람
  private String message; // 메시지
}