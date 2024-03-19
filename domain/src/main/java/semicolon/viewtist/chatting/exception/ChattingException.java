package semicolon.viewtist.chatting.exception;

import semicolon.viewtist.global.exception.CustomException;
import semicolon.viewtist.global.exception.ErrorCode;

public class ChattingException extends CustomException {

  public ChattingException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ChattingException(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }
}
