package semicolon.viewtis.chatting.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomForm {
  private String streamKey;
  private Long streamerId;
  private String studioName;
}
