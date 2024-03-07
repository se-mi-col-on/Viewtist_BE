package semicolon.viewtist.exception;

import lombok.Getter;
import semicolon.viewtist.global.exception.ErrorCode;

@Getter
public class ChattingException extends RuntimeException{
  private final ErrorCode errorCode;

  public ChattingException(ErrorCode errorCode){
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}