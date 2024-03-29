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
public class UpdateNickname {

  @NotBlank(message = "닉네임을 입력해 주세요.")
  private String nickname;

}
