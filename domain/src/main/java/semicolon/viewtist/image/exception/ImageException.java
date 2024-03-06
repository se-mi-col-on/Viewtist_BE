package semicolon.viewtist.image.exception;


import semicolon.viewtist.global.exception.CustomException;
import semicolon.viewtist.global.exception.ErrorCode;

public class ImageException extends CustomException {

  public ImageException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ImageException(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }
}

