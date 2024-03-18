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

  private String nickname;

  public static SubscribeResponse from(Subscribe subscribe) {
    return SubscribeResponse.builder()
        .streamerNickname(subscribe.getReceiver())
        .nickname(subscribe.getUser())
        .build();

  }
}
