package semicolon.viewtist.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.jwt.exception.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserException.class)
  public ResponseEntity<?> handleCategoryException(UserException e) {
    return toResponse(e.getErrorCode(), e.getMessage());
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<?> handleCategoryException(JwtException e) {
    return toResponse(e.getErrorCode(), e.getMessage());
  }

  private static ResponseEntity<ErrorResponse> toResponse(
      ErrorCode errorCode, String message) {
    return ResponseEntity.status(errorCode.getStatus())
        .body(new ErrorResponse(errorCode, message));
  }

}

