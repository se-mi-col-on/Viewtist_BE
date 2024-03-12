package semicolon.viewtist.chatting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.viewtist.chatting.dto.request.ChatRoomRequest;
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
  private boolean activated;

  public static ChatRoom from(ChatRoomRequest request) {
    return ChatRoom.builder()
        .streamKey(request.getStreamKey())
        .streamerId(request.getStreamerId())
        .build();
  }
  public void setChatRoomActivate(boolean status){
    this.activated = status;
  }
}