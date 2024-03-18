package semicolon.viewtist.post.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateTitleRequest {

    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;

}