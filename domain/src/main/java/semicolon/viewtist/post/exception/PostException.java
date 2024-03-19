package semicolon.viewtist.post.exception;

import semicolon.viewtist.global.exception.CustomException;
import semicolon.viewtist.global.exception.ErrorCode;

public class PostException extends CustomException {


    public PostException(ErrorCode errorCode) {
        super(errorCode);
    }

}
