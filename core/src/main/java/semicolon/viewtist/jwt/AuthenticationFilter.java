package semicolon.viewtist.jwt;

import static semicolon.viewtist.global.exception.ErrorCode.INVALID_TOKEN;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import semicolon.viewtist.jwt.exception.JwtException;
import semicolon.viewtist.jwt.repository.TokenBlacklistRepository;

// 인증 필터
@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private static final String TOKEN_PREFIX = "Bearer ";
  private final TokenBlacklistRepository tokenBlacklistRepository;
  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = resolveToken(request);

    if (tokenBlacklistRepository.existsByToken(token)) {
      throw new JwtException(INVALID_TOKEN);
    }

    if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
      Authentication authentication = tokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (ObjectUtils.isEmpty(token) || !token.startsWith(TOKEN_PREFIX)) {
      return null;
    }
    return token.substring(TOKEN_PREFIX.length());
  }
}

