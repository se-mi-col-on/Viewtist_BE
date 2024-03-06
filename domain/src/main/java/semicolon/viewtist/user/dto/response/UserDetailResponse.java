package semicolon.viewtist.user.dto.response;

import lombok.Getter;
import lombok.Setter;
import semicolon.viewtist.user.entity.Account;
import semicolon.viewtist.user.entity.User;

@Getter
@Setter
public class UserDetailResponse {

  private String email;

  private String nickname;

  private String profilePhotoUrl;

  private Account account;

  private String steamKey;

  public UserDetailResponse(User user) {
    this.email = user.getEmail();
    this.nickname = user.getNickname();
    this.profilePhotoUrl = user.getProfilePhotoUrl();
    this.account = user.getAccount();
    this.steamKey = user.getStreamKey();
  }
}
