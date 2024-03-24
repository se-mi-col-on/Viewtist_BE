package semicolon.viewtist.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.service.ChatRoomService;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {
  private final ChatRoomService chatRoomService;
}
