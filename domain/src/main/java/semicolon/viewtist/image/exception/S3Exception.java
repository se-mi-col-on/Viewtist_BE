package semicolon.viewtist.image.exception;


import semicolon.viewtist.global.exception.CustomException;
import semicolon.viewtist.global.exception.ErrorCode;

public class S3Exception extends CustomException {

  public S3Exception(ErrorCode errorCode) {
    super(errorCode);
  }

  public S3Exception(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }
}
