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

  private String profilePhotoUrl;

  private LocalDateTime startAt;

  private Long viewerCount;

  private String streamerStreamKey;

  private String thumbnail;



  public static LiveStreamingResponse from(LiveStreaming liveStreaming) {
    return LiveStreamingResponse.builder()
        .id(liveStreaming.getId())
        .title(liveStreaming.getTitle())
        .streamerNickname(liveStreaming.getUser().getNickname())
        .category(liveStreaming.getCategory())
        .viewerCount(liveStreaming.getViewerCount())
        .profilePhotoUrl(liveStreaming.getUser().getProfilePhotoUrl())
        .startAt(liveStreaming.getStartAt())
        .streamerStreamKey(liveStreaming.getUser().getStreamKey())
        .thumbnail(liveStreaming.getThumbnail())
        .build();
  }
}
