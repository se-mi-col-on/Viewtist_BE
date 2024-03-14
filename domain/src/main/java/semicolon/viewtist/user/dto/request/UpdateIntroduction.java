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
public class UpdateIntroduction {

  @NotBlank(message = "소개글을 입력해 주세요.")
  private String Introduction;

}
