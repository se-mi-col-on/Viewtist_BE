package semicolon.viewtist.user.controller;


import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import semicolon.viewtist.user.dto.request.UserSiginupRequest;
import semicolon.viewtist.user.dto.request.UserSigninRequest;
import semicolon.viewtist.user.service.AuthService;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody UserSiginupRequest request) throws IOException {
    authService.signup(request);
    return ResponseEntity.ok("회원가입 성공적으로 완료되었습니다.");
  }

  // 이메일 전송
  @PostMapping("/send-email")
  public ResponseEntity<String> sendEmail(String email) {
    authService.sendEmail(email);
    return ResponseEntity.ok("이메일 전송 성공적으로 완료되었습니다.");
  }

  // 이메일 인증
  @GetMapping("/verify-email")
  public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
    authService.verifyEmailToken(token);
    return ResponseEntity.ok("이메일이 활성화 되었습니다.");
  }

  // 로그인
  @PostMapping("/signin")
  public ResponseEntity<Map<String, String>> signin(@RequestBody UserSigninRequest request) {
    String token = authService.signin(request);
    Map<String, String> response = Map.of("token", token);
    return ResponseEntity.ok(response);
  }

  // 로그아웃
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/signout")
  public ResponseEntity<String> signout(@RequestHeader("Authorization") String token) {
    authService.signout(token);
    return ResponseEntity.ok("로그아웃이 완료 되었습니다.");
  }

  // 토큰 재발급
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/refresh-token")
  public ResponseEntity<Map<String, String>> refreshToken(
      @RequestHeader("Authorization") String token) {
    String newToken = authService.refreshToken(token);
    Map<String, String> response = Map.of("token", newToken);
    return ResponseEntity.ok(response);
  }


}
