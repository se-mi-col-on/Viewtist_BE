package semicolon.viewtist.jwt;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static semicolon.viewtist.global.exception.ErrorCode.ACCESS_DENIED;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import semicolon.viewtist.global.exception.ErrorResponse;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    ErrorResponse errorResponse = new ErrorResponse(ACCESS_DENIED,
        ACCESS_DENIED.getMessage());

    log.error("{} is occurred in CustomAccessDeniedHandler", errorResponse.errorCode());

    response.setStatus(UNAUTHORIZED.value());
    response.setContentType("application/json;charset=utf-8");
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}