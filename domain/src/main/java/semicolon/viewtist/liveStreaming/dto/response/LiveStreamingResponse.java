package semicolon.viewtist.liveStreaming.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import semicolon.viewtist.liveStreaming.entity.Category;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;

@Getter
@Setter
@Builder
public class LiveStreamingResponse {

  private Long id;

  private String title;

  private Category category;

  private String streamerNickname;

  private LocalDateTime startAt;

  private Long viewerCount;


  public static LiveStreamingResponse from(LiveStreaming liveStreaming) {
    return LiveStreamingResponse.builder()
        .id(liveStreaming.getId())
        .title(liveStreaming.getTitle())
        .streamerNickname(liveStreaming.getUser().getNickname())
        .category(liveStreaming.getCategory())
        .startAt(liveStreaming.getStartAt())
        .build();
  }
}
