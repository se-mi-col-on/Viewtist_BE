package semicolon.viewtist.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import semicolon.viewtist.jwt.exception.JwtException;
import semicolon.viewtist.liveStreaming.exception.LiveStreamingException;
import semicolon.viewtist.user.exception.UserException;


@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(LiveStreamingException.class)
  public ResponseEntity<?> handlerLiveStreamingException(LiveStreamingException e) {
    return toResponse(e.getErrorCode(), e.getMessage());
  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<?> handlerUserException(UserException e) {
    return toResponse(e.getErrorCode(), e.getMessage());
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<?> handlerJwtException(JwtException e) {
    return toResponse(e.getErrorCode(), e.getMessage());
  }

  private static ResponseEntity<ErrorResponse> toResponse(
      ErrorCode errorCode, String message) {
    return ResponseEntity.status(errorCode.getStatus())
        .body(new ErrorResponse(errorCode, message));
  }

}

