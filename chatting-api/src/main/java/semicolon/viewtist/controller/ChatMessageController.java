package semicolon.viewtist.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest;
import semicolon.viewtist.chatting.dto.response.ChatMessageResponse;
import semicolon.viewtist.service.ChatMessageService;
@Slf4j

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatMessageController {
  private final ChatMessageService chatMessageService;
  private final SimpMessageSendingOperations operations;
  @MessageMapping("/channel")
  public void sendMessage(ChatMessageRequest message) {
    operations.convertAndSend
        ("/sub/channel/" + message.getStreamingId(), message);
    log.info("메세지 전송 성공");
  }
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/chat/{streamingId}")
  @Operation(summary = "입장시 이전의 채팅 내역을 불러온다.", description = "채팅방에 있는 채팅 내역들이 불러와짐")
  public ResponseEntity<List<ChatMessageResponse>> getChatMessageHistory(@PathVariable Long streamingId){
    return ResponseEntity.ok(chatMessageService.getChatMessages(streamingId));
  }
}
