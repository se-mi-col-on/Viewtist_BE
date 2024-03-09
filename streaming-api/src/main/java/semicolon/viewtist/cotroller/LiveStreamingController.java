package semicolon.viewtist.cotroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingCreateRequest;
import semicolon.viewtist.liveStreaming.dto.response.LiveStreamingResponse;
import semicolon.viewtist.service.LiveStreamingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/live-streaming")
public class LiveStreamingController {

  private final LiveStreamingService liveStreamingService;

  // 스트리밍 시작
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/start")
  public ResponseEntity<String> startLiveStreaming(
      @RequestBody LiveStreamingCreateRequest liveStreamingCreateRequest,
      Authentication authentication) {
    liveStreamingService.startLiveStreaming(liveStreamingCreateRequest, authentication);
    return ResponseEntity.ok("스트리밍이 시작되었습니다.");
  }

  // 스트리밍 정보
  @GetMapping("/{streamingId}")
  public ResponseEntity<LiveStreamingResponse> liveStreamingPage(@PathVariable Long streamingId) {
    return ResponseEntity.ok(liveStreamingService.liveStreamingPage(streamingId));
  }


}
