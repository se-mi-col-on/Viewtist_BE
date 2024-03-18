package semicolon.viewtist.user.exception;

import semicolon.viewtist.global.exception.CustomException;
import semicolon.viewtist.global.exception.ErrorCode;

public class SubscribeException extends CustomException {

  public SubscribeException(ErrorCode errorCode) {
    super(errorCode);
  }

  public SubscribeException(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }
}
