package semicolon.viewtist.chatting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomRequest {

  private String streamKey;
  private Long streamerId;
  private String studioName;
}
