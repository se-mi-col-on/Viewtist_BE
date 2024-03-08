package semicolon.viewtist.cotroller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingCreateRequest;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingUpdateRequest;
import semicolon.viewtist.liveStreaming.dto.response.LiveStreamingResponse;
import semicolon.viewtist.liveStreaming.entity.Category;
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

  // 스트리밍 업데이트
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/update")
  public ResponseEntity<String> updateLiveStreaming(
      @RequestBody LiveStreamingUpdateRequest liveStreamingUpdateRequest,
      Authentication authentication) {
    liveStreamingService.updateLiveStreaming(liveStreamingUpdateRequest, authentication);
    return ResponseEntity.ok("스트리밍이 업데이트 되었습니다.");
  }

  // 스트리밍 종료
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/stop")
  public ResponseEntity<String> stopLiveStreaming(Authentication authentication) {
    liveStreamingService.stopLiveStreaming(authentication);
    return ResponseEntity.ok("스트리밍이 종료되었습니다.");
  }

  // 스트리밍 나열 시청자 순으로
  @GetMapping()
  public Page<LiveStreamingResponse> getLiveStreamings(
      Pageable pageable) {
    return liveStreamingService.findLiveStreamings(pageable);
  }

  // 카테고리로 검색
  @GetMapping("/category")
  public Page<LiveStreamingResponse> findLiveStteamingsByCategory(@RequestBody Category category,
      Pageable pageable) {
    return liveStreamingService.findLiveStreamingsByCategory(category, pageable);
  }


}
