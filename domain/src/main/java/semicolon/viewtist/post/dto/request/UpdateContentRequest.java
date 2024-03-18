package semicolon.viewtist.post.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContentRequest {

    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

}