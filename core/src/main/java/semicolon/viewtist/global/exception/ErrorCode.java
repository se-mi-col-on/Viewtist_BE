package semicolon.viewtist.global.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  LIVE_STREAMING_NOT_FOUND(NOT_FOUND, "Live Streaming Not Found"),
  ;

  private final HttpStatus status;
  private final String message;
}
