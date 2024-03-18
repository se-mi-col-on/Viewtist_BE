package semicolon.viewtist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import semicolon.viewtist.sse.service.NotifyService;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotifyController {

  private final NotifyService notifyService;


  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public SseEmitter subscribe(Authentication authentication,
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId)
      throws JsonProcessingException {
    return notifyService.subscribe(authentication, lastEventId);
  }

}
