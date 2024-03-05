package semicolon.viewtist.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  LIVE_STREAMING_NOT_FOUND(NOT_FOUND, "라이브 스트리밍을 찾을 수 없습니다"),

  ALREADY_EXISTS_EMAIL(BAD_REQUEST, "이미 존재하는 회원입니다."),
  ALREADY_EXISTS_NICKNAME(BAD_REQUEST, "이미 존재하는 닉네임입니다."),
  USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 회원입니다."),
  EMAIL_NOT_FUND(NOT_FOUND, "존재하지 않는 회원입니다."),
  PASSWORDS_NOT_MATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
  VERIFY_YOUR_EMAIL(BAD_REQUEST, "이메일 인증이 필요합니다."),
  INVALID_TOKEN(BAD_REQUEST, "유효하지 않은 토큰입니다.");

  private final HttpStatus status;
  private final String message;
}
