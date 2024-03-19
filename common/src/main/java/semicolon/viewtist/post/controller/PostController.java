package semicolon.viewtist.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "게시판 작성한다.", description = "게시판 작성한다.")
    public ResponseEntity<PostResponse> write(@RequestBody PostWriteRequest postWriteRequest,
            Authentication authentication) {
        return ResponseEntity.ok(postService.postWrite(postWriteRequest, authentication));
    }

    // updatepost: 게시판 수정
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정한다.", description = "게시글 수정한다.")
    public ResponseEntity<String> updatePost(@PathVariable Long postId,
            @RequestBody UpdatePostRequest updatePostRequest, Authentication authentication) {
        postService.updatePost(postId, updatePostRequest, authentication);
        return ResponseEntity.ok("글이 수정되었습니다.");
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제한다.", description = "게시글 삭제한다.")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, Authentication authentication) {
        postService.deletePost(postId, authentication);
        return ResponseEntity.ok("글이 삭제되었습니다.");
    }

    @GetMapping("/{postId}")
    @Operation(summary = "최신 글을 조회한다.", description = "최신 글을 조회한다.")
    public ResponseEntity<Page<PostResponse>> getAllPost(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(postService.findByPost(pageable));
    }


}
