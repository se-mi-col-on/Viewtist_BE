package semicolon.viewtist.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  ALREADY_EXIST_STREAMKEY(HttpStatus.BAD_REQUEST, "이미 존재하는 스트리밍 방입니다."),
  NOT_EXIST_STREAMKEY(HttpStatus.BAD_REQUEST, "존재하지 않는 스트리밍 방입니다.");
  private final HttpStatus httpStatus;
  private final String message;
}
