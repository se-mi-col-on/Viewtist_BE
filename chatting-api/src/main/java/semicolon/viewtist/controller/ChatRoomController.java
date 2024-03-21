package semicolon.viewtist.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.service.ChatRoomService;
import semicolon.viewtist.websocket.WebSocketChatHandler;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {
  private final ChatRoomService chatRoomService;
  private final WebSocketChatHandler webSocketChatHandler;

  @PreAuthorize("isAuthenticated()")
  @PutMapping("/chatroom/{streamingId}")
  @Operation(summary = "채팅방 상태를 설정한다.", description = "채팅금지 status = ON 채팅가능 status = OFF")
  public ResponseEntity<String> enableChatRoom(@PathVariable Long streamingId, String status, Authentication authentication) throws Exception {

    return ResponseEntity.ok(chatRoomService.setChatRoomStatus(streamingId,status,authentication));
  }
  @GetMapping("/chatroom/{streamingId}")
  @Operation(summary = "실시간 시청자 수를 조회한다.", description = "")
  public ResponseEntity<Integer> getViewerCount(@PathVariable Long streamingId) throws Exception {

    return ResponseEntity.ok(webSocketChatHandler.getViewerCount(streamingId));
  }
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/chatroom/kick/{nickname}")
  @Operation(summary = "회원을 강퇴한다.", description = "")
  public ResponseEntity<?> kickUser (@PathVariable String nickname, Long streamingId, Authentication authentication) throws Exception {
    webSocketChatHandler.kickUser(nickname,streamingId,authentication);
    return ResponseEntity.ok("강퇴되었습니다.");
  }
}
