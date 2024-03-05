package semicolon.viewtist.user.exception;

import semicolon.viewtist.global.exception.CustomException;
import semicolon.viewtist.global.exception.ErrorCode;

public class UserException extends CustomException {

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserException(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }
}
