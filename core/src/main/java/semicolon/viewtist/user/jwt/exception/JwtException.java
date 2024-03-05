package semicolon.viewtist.user.jwt.exception;

import semicolon.viewtist.global.exception.CustomException;
import semicolon.viewtist.global.exception.ErrorCode;

public class JwtException extends CustomException {

  public JwtException(ErrorCode errorCode) {
    super(errorCode);
  }

  public JwtException(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }
}
