package semicolon.viewtist.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import semicolon.viewtist.user.entity.Type;
import semicolon.viewtist.user.entity.User;

@Getter
@Setter
@Builder
public class SocialUserRequest {

  private String userId;
  private Type type;
  private String email;
  private String profilePhotoUrl;

  public static User from(SocialUserRequest socialUserRequest) {
    return User.builder()
        .nickname(socialUserRequest.userId)
        .type(socialUserRequest.type)
        .profilePhotoUrl(socialUserRequest.profilePhotoUrl)
        .email(socialUserRequest.email)
        .password("******")
        .isEmailVerified(true)
        .build();
  }
}
