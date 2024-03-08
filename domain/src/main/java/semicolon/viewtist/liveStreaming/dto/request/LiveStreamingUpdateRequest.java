package semicolon.viewtist.liveStreaming.dto.request;

import lombok.Getter;
import lombok.Setter;
import semicolon.viewtist.liveStreaming.entity.Category;

@Getter
@Setter
public class LiveStreamingUpdateRequest {

  private String updateTitle;

  private Category updateCategory;

}
