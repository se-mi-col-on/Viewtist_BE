package semicolon.viewtist.user.controller;

import java.io.IOException;
import java.util.Map;
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
import semicolon.viewtist.s3.S3UploaderService;
import semicolon.viewtist.user.dto.request.UpdateMypage;
import semicolon.viewtist.user.dto.request.UpdatePasswordRequest;
import semicolon.viewtist.user.dto.response.UserResponse;
import semicolon.viewtist.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final S3UploaderService s3UploaderService;
  private final UserService userService;


  // 마이페이지
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/mypage")
  public ResponseEntity<UserResponse> userDetail(Authentication authentication) {
    return ResponseEntity.ok(userService.userDetail(authentication));
  }


  // 비밀번호 찾기
  @PostMapping("/find-password")
  public ResponseEntity<String> findPassword(@RequestBody String email) {
    userService.resetAndSendTemporaryPassword(email);
    return ResponseEntity.ok("입력된 이메일로 임시비밀번호가 발송 되었습니다.");
  }

  // 비밀번호 변경
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/password")
  public ResponseEntity<String> updatePassword(
      @RequestBody UpdatePasswordRequest updatePasswordRequest, Authentication authentication) {
    userService.updatePassword(updatePasswordRequest, authentication);
    return ResponseEntity.ok("비밀번호가 변경되었습니다.");
  }

//  @PostMapping("/upload")
//  public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file)
//      throws IOException {
//    s3UploaderService.uploadImage(file);
//    return ResponseEntity.ok("사진이 업로드 되었습니다.");
//  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("/update-profile-photo")
  public ResponseEntity<Map<String, String>> updateProfilePhoto(
      @RequestParam("file") MultipartFile file,
      Authentication authentication) throws IOException {
    String photoUrl = userService.updateProfilePhoto(file, authentication);
    Map<String, String> response = Map.of("profilePhotoUrl", photoUrl);
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("/update-mypage")
  public ResponseEntity<String> updateMypage(@RequestBody UpdateMypage updateMypage,
      Authentication authentication) {
    userService.updateUserProfile(updateMypage.getNickname(),
        updateMypage.getChannelIntroduction(), authentication);
    return ResponseEntity.ok("채널관리가 수정되었습니다.");
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/stream-key")
  public ResponseEntity<Map<String, String>> getStreamKey(Authentication authentication) {
    String streamKey = userService.getStreamKey(authentication);
    return ResponseEntity.ok(Map.of("streamKey", streamKey));
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/refresh-stream-key")
  public ResponseEntity<Map<String, String>> refreshStreamKey(Authentication authentication) {
    String newStreamKey = userService.refreshStreamKey(authentication);
    return ResponseEntity.ok(Map.of("streamKey", newStreamKey));
  }
}

