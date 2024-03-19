package semicolon.viewtist.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.service.ChatRoomService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {
  private final ChatRoomService chatRoomService;
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/chatroom/{streamKey}")
  @Operation(summary = "채팅방 상태를 설정한다.", description = "채팅금지 status = ON 채팅가능 status = OFF")
  public ResponseEntity<String> enableChatRoom(@PathVariable String streamKey, String status, Authentication authentication) throws Exception {

    return ResponseEntity.ok(chatRoomService.setChatRoomStatus(streamKey,status,authentication));
  }
}
