package semicolon.viewtist.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingCreateRequest;
import semicolon.viewtist.liveStreaming.dto.request.LiveStreamingThumbnailRequest;
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
  public ResponseEntity<LiveStreamingResponse> startLiveStreaming(
      @RequestBody LiveStreamingCreateRequest liveStreamingCreateRequest,
      Authentication authentication) {
    LiveStreamingResponse liveStreaming = liveStreamingService.startLiveStreaming(
        liveStreamingCreateRequest, authentication);
    return ResponseEntity.ok(liveStreaming);
  }

  // 스트리밍 업데이트
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/{streamingId}")
  public ResponseEntity<String> updateLiveStreaming(@PathVariable Long streamingId,
      @RequestBody LiveStreamingUpdateRequest liveStreamingUpdateRequest,
      Authentication authentication) {
    liveStreamingService.updateLiveStreaming(streamingId, liveStreamingUpdateRequest,
        authentication);
    return ResponseEntity.ok("스트리밍이 업데이트 되었습니다.");
  }

  // 스트리밍 정보 보기
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{streamingId}")
  public ResponseEntity<LiveStreamingResponse> getLiveStreaming(@PathVariable Long streamingId) {
    LiveStreamingResponse liveStreaming = liveStreamingService.getLiveStreaming(streamingId);
    return ResponseEntity.ok(liveStreaming);
  }

  // 스트리밍 나열 시청자 순으로
  @GetMapping("/all-streaming")
  public Page<LiveStreamingResponse> getLiveStreamings(
      Pageable pageable) {
    return liveStreamingService.findLiveStreamings(pageable);
  }

  // 카테고리로 검색
  @GetMapping("/category")
  public Page<LiveStreamingResponse> findLiveSteamingsByCategory(@RequestParam Category category,
      Pageable pageable) {
    return liveStreamingService.findLiveStreamingsByCategory(category, pageable);
  }

  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{streamingId}")
  public ResponseEntity<String> stopStreaming(@PathVariable Long streamingId,
      Authentication authentication) {
    liveStreamingService.stopStreaming(streamingId, authentication);
    return ResponseEntity.ok("스트리밍이 종료되었습니다.");
  }

  @GetMapping("/search")
  public ResponseEntity<Page<LiveStreamingResponse>> searchLiveStreamings(
      @RequestParam(value = "keyword") String keyword, Pageable pageable) {
    return ResponseEntity.ok(liveStreamingService.findLiveStreaming(keyword, pageable));
  }

  @PutMapping("/thumbnail/{streamingId}")
  public ResponseEntity<LiveStreamingResponse> thumbnail(@PathVariable Long streamingId,
      @RequestBody LiveStreamingThumbnailRequest thumbnail) {
    LiveStreamingResponse liveStreaming = liveStreamingService.thumbnail(streamingId, thumbnail);
    return ResponseEntity.ok(liveStreaming);
  }


  @GetMapping("/viewers/{streamingId}")
  @Operation(summary = "현재 스트리밍을 보고 있는 시청자 수를 조회한다.", description = "")
  public ResponseEntity<Long> getViewCount(@PathVariable Long streamingId){
    return ResponseEntity.ok(liveStreamingService.getViewerCount(streamingId));
  }
}
