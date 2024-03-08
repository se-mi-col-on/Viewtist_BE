package semicolon.viewtist.liveStreaming.exception;

import semicolon.viewtist.global.exception.CustomException;
import semicolon.viewtist.global.exception.ErrorCode;

public class LiveStreamingException extends CustomException {

  public LiveStreamingException(ErrorCode errorCode) {
    super(errorCode);
  }

  public LiveStreamingException(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }
}
