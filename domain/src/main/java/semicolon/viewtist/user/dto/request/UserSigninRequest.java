package semicolon.viewtist.user.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSigninRequest {

  @NotBlank(message = "이메일을 입력해 주세요")
  @Email(message = "이메일 형식으로 입력해 주세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해 주세요.")
  @Pattern(regexp = "[a-zA-Z1-9]{8,16}",
      message = "비밀번호는 영어와 숫자를 포함해서 8~16자리 입니다.")
  private String password;

}
