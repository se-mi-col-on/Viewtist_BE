package semicolon.viewtist.post.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import semicolon.viewtist.post.dto.request.PostWriteRequest;
import semicolon.viewtist.post.dto.request.UpdatePostRequest;
import semicolon.viewtist.post.dto.response.PostResponse;
import semicolon.viewtist.post.entity.Post;
import semicolon.viewtist.post.repository.PostRepository;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    public PostResponse postWrite(PostWriteRequest postWriteRequest) {
        // 게시물 작성 로직을 구현
        Post post= Post.builder()
                .title(postWriteRequest.getTitle())
                .content(postWriteRequest.getContent())
                .build();

        postRepository.save(post);
        return PostResponse.from(post);
    }

    @Transactional
    public void updatePost(Long postId, UpdatePostRequest updatePostRequest,
            Authentication authentication) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 글을 찾을 수 없습니다."));

        // 사용자의 인증 정보를 이용하여 사용자를 식별
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 글을 찾을 수 없습니다."));

        // 현재 글의 작성자와 로그인한 사용자의 ID를 비교하여 같은 사용자인지 확인
        if (Objects.equals(post.getUser().getId(), user.getId())) {
            post.setTitle(updatePostRequest.getTitle());
            post.setContent(updatePostRequest.getContent());
            postRepository.save(post);
        } else {
            throw new AccessDeniedException("해당 글에 대한 권한이 없습니다.");
        }
    }

}
