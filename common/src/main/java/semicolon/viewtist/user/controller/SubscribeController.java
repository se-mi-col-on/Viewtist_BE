package semicolon.viewtist.user.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import semicolon.viewtist.user.service.SubscriberService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubscribeController {
  private final SubscriberService subscriberService;
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/subscribe/{streamerId}")
  public ResponseEntity<?> followStreamer(@PathVariable Long streamerId, Authentication authentication){
    subscriberService.subscribeStreamer(streamerId, authentication);
    return ResponseEntity.ok("구독이 완료되었습니다.");
  }
  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/subscribe/{streamerId}")
  public ResponseEntity<?> cancelSubscribe(@PathVariable Long streamerId, Authentication authentication){
    subscriberService.cancelSubscribe(streamerId, authentication);
    return ResponseEntity.ok("구독이 취소되었습니다.");
  }

}
