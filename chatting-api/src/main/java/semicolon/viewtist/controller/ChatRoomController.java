package semicolon.viewtist.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.service.ChatRoomService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {
  private final ChatRoomService chatRoomService;
  @GetMapping("/view/{streamingId}")
  @Operation(summary = "현재 시청자 수를 조회한다.", description = "")
  public ResponseEntity<Long> getViewCount(@PathVariable Long streamingId){
    return ResponseEntity.ok(chatRoomService.getViews(streamingId));
  }
}
