package semicolon.viewtist.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import semicolon.viewtist.chatting.entity.ChatRoom;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {

  private String streamKey;
  private Long streamerId;

  public static ChatRoomResponse from(ChatRoom chatroom) {
    return ChatRoomResponse.builder()
        .streamerId(chatroom.getStreamerId())
        .build();
  }
}