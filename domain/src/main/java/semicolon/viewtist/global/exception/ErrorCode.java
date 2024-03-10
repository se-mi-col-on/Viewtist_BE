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
  EMAIL_NOT_FOUND(NOT_FOUND, "존재하지 않는 회원입니다."),
  PASSWORDS_NOT_MATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
  VERIFY_YOUR_EMAIL(BAD_REQUEST, "이메일 인증이 필요합니다."),
  INVALID_TOKEN(BAD_REQUEST, "유효하지 않은 토큰입니다."),
  TIME_OUT_INVALID_TOKEN(BAD_REQUEST, "인증시간이 만료되었습니다."),
  NOT_VERIFIED_EMAIL(BAD_REQUEST, "이메일 인증을 진행해 주세요."),

  // s3
  S3_FILE_CONVERT_ERROR(BAD_REQUEST, "S3 파일변환 에러입니다."),
  S3_FILE_DELETE_ERROR(BAD_REQUEST, "S3 파일 삭제 에러입니다."),
  IMAGE_INFORMATION_DB_NOT_FOUND(NOT_FOUND, "이미지 정보를 찾을 수 없습니다."),
  // image

  // chatting
  ALREADY_EXIST_STREAMKEY(HttpStatus.BAD_REQUEST, "이미 존재하는 스트리밍 방입니다."),
  NOT_EXIST_STREAMKEY(HttpStatus.BAD_REQUEST, "존재하지 않는 스트리밍 방입니다.");

  private final HttpStatus status;
  private final String message;
}
