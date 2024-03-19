package semicolon.viewtist.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.jwt.exception.JwtException;
import semicolon.viewtist.post.exception.PostException;
import semicolon.viewtist.liveStreaming.exception.LiveStreamingException;
import semicolon.viewtist.user.exception.SubscribeException;
import semicolon.viewtist.user.exception.UserException;


@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(SubscribeException.class)
  public ResponseEntity<?> handlerSubscribeException(SubscribeException e) {
    return toResponse(e.getErrorCode(), e.getMessage());
  }

  @ExceptionHandler(LiveStreamingException.class)
  public ResponseEntity<?> handlerLiveStreamingException(LiveStreamingException e) {
    return toResponse(e.getErrorCode(), e.getMessage());
  }

  @ExceptionHandler(ChattingException.class)
  public ResponseEntity<?> handlerChattingException(ChattingException e) {
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

  @ExceptionHandler(PostException.class)
  public ResponseEntity<?> handlerPostException(PostException e) {
    return toResponse(e.getErrorCode(), e.getMessage());
  }

  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<?> handlerGlobalException(GlobalException e) {
    return toResponse(e.getErrorCode(), e.getMessage());
  }

  private static ResponseEntity<ErrorResponse> toResponse(
      ErrorCode errorCode, String message) {
    return ResponseEntity.status(errorCode.getStatus())
        .body(new ErrorResponse(errorCode, message));
  }

}

