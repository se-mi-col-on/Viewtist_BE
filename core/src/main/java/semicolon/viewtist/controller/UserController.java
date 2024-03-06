package semicolon.viewtist.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import semicolon.viewtist.user.dto.request.UpdatePasswordRequest;
import semicolon.viewtist.user.dto.response.UserDetailResponse;
import semicolon.viewtist.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;


  // 마이페이지
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/mypage")
  public ResponseEntity<UserDetailResponse> userDetail(Authentication authentication) {
    return ResponseEntity.ok(userService.userDetail(authentication));
  }

  // 프로필 사진 변경
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/profile-photo")
  public ResponseEntity<String> updateProfilePhoto(@RequestParam("file") MultipartFile file,
      Authentication authentication)
      throws IOException {
    userService.updateProfilePhoto(file, authentication);
    return ResponseEntity.ok("프로필 사진이 변경되었습니다.");
  }

  // 비밀번호 찾기
  @PostMapping("/find-password")
  public ResponseEntity<String> findPassword(@RequestBody String email) {
    userService.findPassword(email);
    return ResponseEntity.ok("입력된 이메일로 임시비밀번호가 발송 되었습니다.");
  }

  // 비밀번호 변경
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/password")
  public ResponseEntity<Void> updatePassword(
      @RequestBody UpdatePasswordRequest updatePasswordRequest, Authentication authentication) {
    userService.updatePassword(updatePasswordRequest, authentication);
    return ResponseEntity.ok().build();
  }

  // 스트림키 재발급
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/refresh-stream-key")
  public ResponseEntity<String> updateStreamKey(Authentication authentication) {
    return ResponseEntity.ok(userService.reissueStreamKey(authentication));
  }
}

