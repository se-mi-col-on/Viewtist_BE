package semicolon.viewtist.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import semicolon.viewtist.post.dto.request.PostWriteRequest;
import semicolon.viewtist.post.dto.request.UpdateContentRequest;
import semicolon.viewtist.post.dto.request.UpdateTitleRequest;
import semicolon.viewtist.post.dto.response.PostResponse;
import semicolon.viewtist.post.service.PostService;

@Controller
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // postwriteform: 게시판 작성 페이지 이동
    @GetMapping("/postwriteform")
    public String postwriteform() {
        return "postWrite";
    }

    // postwrite: 게시판 작성
    @PostMapping("/postwrite")
    public ResponseEntity<PostResponse> postwrite(@RequestBody PostWriteRequest postWriteRequest) {
        return ResponseEntity.ok(postService.postwrite(postWriteRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updateposttitle")
    public ResponseEntity<String> updateposttitle(@RequestBody UpdateTitleRequest updateTitleRequest,
            Authentication authentication) {
        postService.updateposttitle(updateTitleRequest, authentication);
        return ResponseEntity.ok("제목이 수정되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updatepostscontent")
    public ResponseEntity<String> updatepostscontent(@RequestBody UpdateContentRequest updateContentRequest,
            Authentication authentication) {
        postService.updatepostscontent(updateContentRequest, authentication);
        return ResponseEntity.ok("내용이 수정되었습니다.");
    }

}
