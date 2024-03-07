package semicolon.viewtist.user.handler;

import static semicolon.viewtist.global.exception.ErrorCode.USER_NOT_FOUND;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import semicolon.viewtist.jwt.TokenProvider;
import semicolon.viewtist.user.entity.CustomOAuth2User;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final UserRepository userRepository;
  private final TokenProvider tokenProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    String userId = oAuth2User.getName();
    User user = userRepository.findByNickname(userId)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    String token = tokenProvider.generateToken(user);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    JSONObject jsonResponse = new JSONObject();
    jsonResponse.put("token", token);

    response.getWriter().write(jsonResponse.toString());
  }

}