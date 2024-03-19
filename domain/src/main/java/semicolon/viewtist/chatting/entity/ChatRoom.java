package semicolon.viewtist.chatting.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.viewtist.global.entitiy.BaseTimeEntity;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;
import semicolon.viewtist.user.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ChatRoom extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name="streaming_id")
  private LiveStreaming streaming;
  private Long streamerId;
  private boolean active;


  public static ChatRoom madeByUser(User user) {
    return ChatRoom.builder()
        .streamerId(user.getId())
        .active(true)
        .build();
  }

  public void setChatRoomActivate(boolean status){
    this.active = status;
  }
  public boolean isActive(){
    return active;
  }
  public void createdByStreaming(LiveStreaming liveStreaming){
    this.streaming = liveStreaming;
  }
}