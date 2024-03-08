package semicolon.viewtist.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest;
import semicolon.viewtist.chatting.dto.request.ChatMessageRequest.MessageType;
import semicolon.viewtist.chatting.dto.response.ChatRoomResponse;
import semicolon.viewtist.chatting.dto.request.ChatRoomRequest;
import semicolon.viewtist.service.ChatService;
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {
  private final ChatService chatService;
  private final SimpMessageSendingOperations sendingOperations;

// 스트리밍 시작시 채팅방 생성
  @PostMapping("/chatroom")
  public ResponseEntity<String> createChatroom(
      @RequestBody ChatRoomRequest chatRoomRequest) throws Exception {
    chatService.createRoom(chatRoomRequest);
    return ResponseEntity.ok("방이 생성되었습니다.");
  }
  @GetMapping("/chatroom")
  public ResponseEntity< List<ChatRoomResponse>> getAllChatroom() throws Exception {
    return ResponseEntity.ok(chatService.findAllRoom());
  }

  @MessageMapping("/message")
  public void enter(ChatMessageRequest message) {
    if (MessageType.ENTER.equals(message.getMessageType())) {
      message.setMessage(message.getSenderId()+"님이 입장하였습니다.");
    }
    sendingOperations.convertAndSend("/topic/chat/room/"+message.getStreamKey(),message);
  }

  }

