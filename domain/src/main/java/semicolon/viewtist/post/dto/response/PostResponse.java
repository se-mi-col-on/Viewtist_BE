package semicolon.viewtist.post.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import semicolon.viewtist.post.entity.Post;

@Getter
@Setter
@Builder
public class PostResponse {

  private Long id;

  private String title;

  private String content;

  private String nickname;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public static PostResponse from(Post post) {
      return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(post.getUser().getNickname())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
  }

}