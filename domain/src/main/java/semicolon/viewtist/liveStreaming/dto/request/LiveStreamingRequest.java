package semicolon.viewtist.liveStreaming.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import semicolon.viewtist.liveStreaming.entity.Category;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;
import semicolon.viewtist.user.entity.User;

@Getter
@Setter
public class LiveStreamingRequest {

  private String title;

  private Category category;

  public LiveStreaming from(LiveStreamingRequest liveStreamingRequest, User user) {
    return LiveStreaming.builder()
        .title(liveStreamingRequest.getTitle())
        .category(liveStreamingRequest.getCategory())
        .user(user)
        .startAt(LocalDateTime.now())
        .build();
  }
}
