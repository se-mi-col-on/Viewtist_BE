package semicolon.viewtist.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import semicolon.viewtist.chatting.entity.ChatRoom;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {

  private String streamKey;
  private String studioName;

  public static ChatRoomResponse from(ChatRoom chatroom) {
    return ChatRoomResponse.builder()
        .streamKey(chatroom.getStreamKey())
        .studioName(chatroom.getStudioName())
        .build();
  }
}