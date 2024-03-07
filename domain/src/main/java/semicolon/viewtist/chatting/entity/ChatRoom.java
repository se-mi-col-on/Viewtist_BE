package semicolon.viewtist.chatting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.viewtist.chatting.form.ChatRoomForm;
import semicolon.viewtist.global.entitiy.BaseTimeEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
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