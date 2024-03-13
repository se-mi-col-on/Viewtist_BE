package semicolon.viewtist.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import semicolon.viewtist.chatting.dto.request.ChatRoomRequest;
import semicolon.viewtist.chatting.entity.ChatRoom;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;

@SpringBootTest
class ChatRoomServiceTest {
  @Autowired
  private ChatRoomService chatRoomService;
  @Autowired
  private ChatRoomRepository chatRoomRepository;
@Test
void CREATE_ROOM(){
    // given
  String streamKey = "test";
  ChatRoomRequest chatRoomRequest = ChatRoomRequest.builder().streamKey(streamKey).streamerId(1L).build();
    // when
  chatRoomService.createRoom(chatRoomRequest);
    // then
  assertEquals("test",chatRoomRepository.findByStreamKey(streamKey).get().getStreamKey());
}
}