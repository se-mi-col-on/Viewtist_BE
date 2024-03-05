package semicolon.viewtist.user.jwt;

import static semicolon.viewtist.global.exception.ErrorCode.VERIFY_YOUR_EMAIL;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import semicolon.viewtist.user.dto.UserDto;
import semicolon.viewtist.user.exception.UserException;

@RequiredArgsConstructor
@Component
public class TokenProvider {

  @Value("${spring.jwt.key}")
  private String key;
  private SecretKey secretKey;
  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간

  @PostConstruct
  private void setSecretKey() {
    secretKey = Keys.hmacShaKeyFor(key.getBytes());
  }

  // 토큰생성
  public String generateToken(UserDto userDto) {
    if (!userDto.isEmailVerified()) {
      throw new UserException(VERIFY_YOUR_EMAIL);
    }

    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    return Jwts.builder()
        .setSubject(userDto.getEmail())
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  // 토큰을 통한 유저 인증 정보 생성
  public Authentication getAuthentication(String token) {
    Claims claims = parseClaims(token);

    User user = new User(claims.getSubject(), "", Collections.emptyList());
    return new UsernamePasswordAuthenticationToken(user, token, Collections.emptyList());
  }

  // 토큰 유효성 검증
  public boolean validateToken(String token) {
    if (StringUtils.hasText(token)) {
      Claims claims = parseClaims(token);
      return claims.getExpiration().after(new Date());
    }

    return false;
  }

  // 토큰 복호화
  private Claims parseClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
