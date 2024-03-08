package semicolon.viewtist.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import semicolon.viewtist.user.entity.User;

@Getter
@Setter
@Builder
public class UserResponse {

  private String email;

  private String nickname;

  private String profilePhotoUrl;

  private Long accountId;


  public static UserResponse from(User user) {

    return UserResponse.builder()
        .email(user.getEmail())
        .nickname(user.getNickname())
        .profilePhotoUrl(user.getProfilePhotoUrl())
        .accountId(user.getAccountId())
        .build();
  }
}
