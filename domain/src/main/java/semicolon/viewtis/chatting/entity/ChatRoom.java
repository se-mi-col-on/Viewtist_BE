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
import semicolon.viewtis.chatting.form.ChatRoomForm;
import semicolon.viewtis.core.global.BaseTimeEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@AuditOverride(forClass = BaseTimeEntity.class)
public class ChatRoom extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String streamKey;
  private Long streamerId;
  private String studioName;

  public static ChatRoom from(ChatRoomForm form){
    return ChatRoom.builder()
        .streamKey(form.getStreamKey())
        .streamerId(form.getStreamerId())
        .studioName(form.getStudioName())
        .build();
  }
}