package semicolon.viewtist.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

  @MessageMapping("/message")
  public void sendMessage(ChatMessageRequest chatMessageRequest) {
    ChatMessageResponse chatMessage = chatMessageService.manageChatMessage(chatMessageRequest);
    operations.convertAndSend
        ("/sub/room/" + chatMessage.getStreamingId(), chatMessage);
  }

  @GetMapping("/chat/{streamingId}")
  @Operation(summary = "입장시 이전의 채팅 내역을 불러온다.", description = "채팅방에 있는 채팅 내역들이 불러와짐")
  public ResponseEntity<Page<ChatMessageResponse>> getChatMessageHistory(@PathVariable Long streamingId, Pageable pageable){
    return ResponseEntity.ok(chatMessageService.getChatMessages(streamingId,pageable));
  }
}
