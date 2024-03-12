package semicolon.viewtist.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.chatting.dto.request.ChatRoomRequest;
import semicolon.viewtist.chatting.dto.response.ChatRoomResponse;
import semicolon.viewtist.service.ChatRoomService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {
  private final ChatRoomService chatRoomService;
  // 스트리밍 시작시 채팅방 생성
  @PostMapping("/chatroom")
  @Operation(summary = "채팅방 생성", description = "처음 스트리밍을 시작할때 생성됨 status 자동으로 ON")
  public ResponseEntity<String> createChatRoom(
      @RequestBody ChatRoomRequest chatRoomRequest) throws Exception {
    chatRoomService.createRoom(chatRoomRequest);
    return ResponseEntity.ok("채팅방이 생성되었습니다.");
  }
  @PutMapping("/chatroom/{streamKey}")
  @Operation(summary = "채팅방 상태 설정", description = "스트리밍 시작할때는 status = ON 종료할때는 status = OFF")
  public ResponseEntity<String> enableChatRoom(@PathVariable String streamKey, String status) throws Exception {
    chatRoomService.setChatRoomStatus(streamKey,status);
    return ResponseEntity.ok("채팅방이 종료되었습니다.");
  }
  @GetMapping("/chatroom")
  @Operation(summary = "현재 활성화된 채팅방을 조회한다.", description = "스트리밍방송이 켜져있는 채팅방 조회.")
  public ResponseEntity< List<ChatRoomResponse>> getAllChatroom() throws Exception {
    return ResponseEntity.ok(chatRoomService.findActivatedRoom());
  }
}
