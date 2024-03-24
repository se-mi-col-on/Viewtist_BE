package semicolon.viewtist.liveStreaming.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.viewtist.chatting.entity.ChatRoom;
import semicolon.viewtist.global.entitiy.BaseTimeEntity;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingUpdateRequest;
import semicolon.viewtist.liveStreaming.dto.response.LiveStreamingResponse;
import semicolon.viewtist.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiveStreaming extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private User user;

  @Column
  private String title;

  @Column
  @Enumerated(EnumType.STRING)
  private Category category;

  @Column
  private LocalDateTime startAt;

  @Column
  private Long viewerCount;

  @OneToOne(mappedBy = "streaming",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  private ChatRoom chatRoom;

  @Column(columnDefinition = "TEXT")
  private String thumbnail;




  public LiveStreamingResponse from(LiveStreaming liveStreaming) {
    return LiveStreamingResponse.builder()
        .title(liveStreaming.getTitle())
        .streamerNickname(liveStreaming.getUser().getNickname())
        .category(liveStreaming.getCategory())
        .startAt(liveStreaming.getStartAt())
        .viewerCount(liveStreaming.getViewerCount())
        .thumbnail(liveStreaming.getThumbnail())
        .build();
  }

  public void update(LiveStreamingUpdateRequest liveStreamingUpdateRequest) {
    this.title = liveStreamingUpdateRequest.getUpdateTitle();
    this.category = liveStreamingUpdateRequest.getUpdateCategory();
  }

  public void createChatRoom(ChatRoom chatRoom){
    this.chatRoom = chatRoom;
    chatRoom.createdByStreaming(this);
  }

  public void updateThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public void addViewCount(){
    this.viewerCount++;
  }
  public void minusViewCount(){
    this.viewerCount--;
  }
}
