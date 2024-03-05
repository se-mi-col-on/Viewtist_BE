package semicolon.viewtist.user.controller;


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

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody UserSiginupRequest request) {
    authService.signup(request);
    return ResponseEntity.ok("회원가입 성공적으로 완료되었습니다.");
  }

  @GetMapping("/verify-email")
  public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
    authService.verifyEmailToken(token);
    return ResponseEntity.ok("이메일이 활성화 되었습니다.");
  }

  @PostMapping("/signin")
  public ResponseEntity<String> signin(@RequestBody UserSigninRequest request) {
    String token = authService.signin(request);
    return ResponseEntity.ok(token);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/signout")
  public ResponseEntity<String> signout(@RequestHeader("Authorization") String token) {
    authService.signout(token);
    return ResponseEntity.ok("로그아웃이 완료 되었습니다.");
  }

}
