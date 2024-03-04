package semicolon.viewtist.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static ResponseEntity<ErrorResponse> toResponse(
      ErrorCode errorCode, String message) {
    return ResponseEntity.status(errorCode.getStatus())
        .body(new ErrorResponse(errorCode, message));
  }

}

