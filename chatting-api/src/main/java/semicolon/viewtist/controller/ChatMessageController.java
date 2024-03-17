package semicolon.viewtist.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest;
import semicolon.viewtist.chatting.dto.response.ChatMessageResponse;
import semicolon.viewtist.service.ChatMessageService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatMessageController {
  private final ChatMessageService chatMessageService;
  @MessageMapping("/message")
  public void enter(ChatMessageRequest message) {
    chatMessageService.sendChatMessage(message);
  }
  @GetMapping("/chat/{streamKey}")
  @Operation(summary = "입장시 이전의 채팅 내역을 불러온다.", description = "채팅방에 있는 채팅 내역들이 불러와짐")
  public ResponseEntity<List<ChatMessageResponse>> getChatMessageHistory(@PathVariable String streamKey){
    return ResponseEntity.ok(chatMessageService.getChatMessages(streamKey));
  }
}
