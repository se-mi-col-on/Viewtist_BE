package semicolon.viewtist.liveStreaming.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import semicolon.viewtist.liveStreaming.entity.Category;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;
import semicolon.viewtist.user.entity.User;

@Getter
@Setter
public class LiveStreamingCreateRequest {

  private String title;

  private Category category;


  public LiveStreaming from(LiveStreamingCreateRequest liveStreamingCreateRequest, User user) {
    return LiveStreaming.builder()
        .title(liveStreamingCreateRequest.getTitle())
        .user(user)
        .category(liveStreamingCreateRequest.getCategory())
        .startAt(LocalDateTime.now())
        .build();
  }
}
