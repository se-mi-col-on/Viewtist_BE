package semicolon.viewtist.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.chatting.dto.ChatRoomDto;
import semicolon.viewtist.chatting.form.ChatRoomForm;
import semicolon.viewtist.service.LiveChatService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LiveChatController {
  private final LiveChatService liveChatService;

// 스트리밍 시작시 채팅방 생성
  @PostMapping("/chatroom")
  public ResponseEntity<String> createChatroom(
      @RequestBody ChatRoomForm chatRoomForm) throws Exception {
    return ResponseEntity.ok(liveChatService.createChatroom(chatRoomForm));
  }
  @GetMapping("/chatroom")
  public ResponseEntity< List<ChatRoomDto>> getAllChatroom() throws Exception {
    return ResponseEntity.ok(liveChatService.getAllChatroom());
  }
}
