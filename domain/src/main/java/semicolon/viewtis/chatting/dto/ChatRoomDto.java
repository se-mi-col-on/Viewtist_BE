package semicolon.viewtis.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import semicolon.viewtis.chatting.entity.ChatRoom;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
  private String streamKey;
  private String studioName;

  public static ChatRoomDto from(ChatRoom chatroom){
    return ChatRoomDto.builder()
        .streamKey(chatroom.getStreamKey())
        .studioName(chatroom.getStudioName())
        .build();
  }
}