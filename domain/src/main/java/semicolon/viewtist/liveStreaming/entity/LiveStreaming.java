package semicolon.viewtist.liveStreaming.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.viewtist.global.entitiy.BaseTimeEntity;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingRequest;
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
  private Category category;

  @Column
  private LocalDateTime startAt;

  @Column
  private Long viewerCount;

  public LiveStreamingResponse form(LiveStreaming liveStreaming) {
    return LiveStreamingResponse.builder()
        .title(liveStreaming.getTitle())
        .streamerNickname(liveStreaming.getUser().getNickname())
        .category(liveStreaming.getCategory())
        .startAt(liveStreaming.getStartAt())
        .viewerCount(liveStreaming.getViewerCount())
        .build();
  }

  public void update(LiveStreamingRequest liveStreamingRequest) {
    this.title = liveStreamingRequest.getTitle();
    this.category = liveStreamingRequest.getCategory();
  }
}
