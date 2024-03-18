package semicolon.viewtist.post.service;

import static semicolon.viewtist.global.exception.ErrorCode.POST_NOT_FOUND;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import semicolon.viewtist.post.dto.request.PostWriteRequest;
import semicolon.viewtist.post.dto.request.UpdateContentRequest;
import semicolon.viewtist.post.dto.request.UpdateTitleRequest;
import semicolon.viewtist.post.dto.response.PostResponse;
import semicolon.viewtist.post.entity.Post;
import semicolon.viewtist.post.exception.PostException;
import semicolon.viewtist.post.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private PostRepository postRepository;

    private Post findByIdOrThrow(Long Id) {
        return postRepository.findById(Id)
            .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }


    public PostResponse postwrite(PostWriteRequest postWriteRequest) {
        // 게시물 작성 로직을 구현
        Post post= Post.builder()
                .title(postWriteRequest.getTitle())
                .content(postWriteRequest.getContent())
                .build();

        postRepository.save(post);
        return PostResponse.from(post);
    }



    @Transactional
    public void updateposttitle(UpdateTitleRequest updateTitleRequest,
            Authentication authentication) {
        Post post = findByIdOrThrow(Long.valueOf(authentication.getName()));
        post.setTitle(updateTitleRequest.getTitle());
        postRepository.save(post);
    }

    @Transactional
    public void updatepostscontent(UpdateContentRequest updateContentRequest,
            Authentication authentication) {
        Post post = findByIdOrThrow(Long.valueOf(authentication.getName()));
        post.setContent(updateContentRequest.getContent());
        postRepository.save(post);
    }


}
