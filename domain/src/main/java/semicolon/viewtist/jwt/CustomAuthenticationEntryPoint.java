package semicolon.viewtist.jwt;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static semicolon.viewtist.global.exception.ErrorCode.SECURITY_UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import semicolon.viewtist.global.exception.ErrorResponse;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    ErrorResponse errorResponse = new ErrorResponse(SECURITY_UNAUTHORIZED,
        SECURITY_UNAUTHORIZED.getMessage());

    log.error("{} is occurred in CustomAuthenticationEntryPoint", errorResponse.errorCode());

    response.setStatus(UNAUTHORIZED.value());
    response.setContentType("application/json;charset=utf-8");
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}