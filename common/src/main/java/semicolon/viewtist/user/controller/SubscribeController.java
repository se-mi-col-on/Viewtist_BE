package semicolon.viewtist.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.user.dto.response.SubscribeResponse;
import semicolon.viewtist.user.service.SubscribeService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SubscribeController {

  private final SubscribeService subscribeService;

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/subscribe")
  public ResponseEntity<String> subscribe(@RequestBody String streamerNickname,
      Authentication authentication) {
    subscribeService.subscribe(streamerNickname, authentication);
    return ResponseEntity.ok("구독이 완료되었습니다.");
  }


  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/unsubscribe")
  public ResponseEntity<String> unsubscribe(@RequestBody String streamerNickname,
      Authentication authentication) {
    subscribeService.unsubscribe(streamerNickname, authentication);
    return ResponseEntity.ok("구독이 취소되었습니다.");
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/subscribe")
  public Page<SubscribeResponse> getSubscribeList(Authentication authentication, Pageable pageable) {
    return subscribeService.getSubscribeList(authentication,pageable);
  }


}
