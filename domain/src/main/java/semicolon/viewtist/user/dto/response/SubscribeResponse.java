package semicolon.viewtist.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.viewtist.sse.entity.Subscribe;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscribeResponse {

  private String streamerNickname;
  private String profilephotoURL;

  //private String nickname;

  public static SubscribeResponse from(String streamerNickname, String profilephotoURL) {
    return SubscribeResponse.builder()
        .streamerNickname(streamerNickname)
        .profilephotoURL(profilephotoURL)
        .build();
  }
}
