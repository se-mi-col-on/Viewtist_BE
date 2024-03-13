package semicolon.viewtist.user.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMypage {

  @NotBlank(message = "닉네임을 입력해 주세요.")
  private String nickname;

  @NotBlank(message = "채널 소개를 적어주세요")
  private String channelIntroduction;

}
