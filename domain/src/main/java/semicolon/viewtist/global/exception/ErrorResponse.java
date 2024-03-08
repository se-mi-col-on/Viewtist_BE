package semicolon.viewtist.global.exception;

public record ErrorResponse(
    ErrorCode errorCode,
    String message
) {

}
