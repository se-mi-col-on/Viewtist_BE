package semicolon.viewtist.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import semicolon.viewtist.post.dto.request.PostWriteRequest;
import semicolon.viewtist.post.dto.request.UpdatePostRequest;
import semicolon.viewtist.post.dto.response.PostResponse;
import semicolon.viewtist.post.service.PostService;

@Controller
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // postwrite: 게시판 작성
    @PostMapping("/write")
    public ResponseEntity<PostResponse> write(@RequestBody PostWriteRequest postWriteRequest) {
        return ResponseEntity.ok(postService.postWrite(postWriteRequest));
    }

    // updatepost: 게시판 수정
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId,
            @RequestBody UpdatePostRequest updatePostRequest, Authentication authentication) {
        postService.updatePost(postId, updatePostRequest, authentication);
        return ResponseEntity.ok("글이 수정되었습니다.");
    }


}
