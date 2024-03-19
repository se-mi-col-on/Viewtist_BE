package semicolon.viewtist.user.controller;

import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import semicolon.viewtist.user.dto.request.UpdateIntroduction;
import semicolon.viewtist.user.dto.request.UpdateNickname;
import semicolon.viewtist.user.dto.request.UpdatePasswordRequest;
import semicolon.viewtist.user.dto.response.UserResponse;
import semicolon.viewtist.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

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
  @PutMapping(value = "/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> updateProfilePhoto(@Parameter(
      description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다. 이때 key 값은 multipartFile 입니다."
  )
  @RequestPart("file") MultipartFile file,
      Authentication authentication) throws IOException {
    String photoUrl = userService.updateProfilePhoto(file, authentication);
    return ResponseEntity.ok(photoUrl);
  }

  // 프로필 사진 초기화
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/reset-profile-photo")
  public ResponseEntity<String> resetProfilePhoto(Authentication authentication) {
    String photoUrl = userService.resetProfilePhoto(authentication);
    return ResponseEntity.ok(photoUrl);
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("/nickname")
  public ResponseEntity<String> updateMypage(@RequestBody UpdateNickname updateNickname,
      Authentication authentication) {
    userService.updateNickname(updateNickname, authentication);
    return ResponseEntity.ok("닉네임이 수정되었습니다.");
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("/introduction")
  public ResponseEntity<String> updateIntroduction(
      @RequestBody UpdateIntroduction updateIntroduction,
      Authentication authentication) {
    userService.updateIntroduction(updateIntroduction, authentication);
    return ResponseEntity.ok("소개글이 수정되었습니다.");
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/stream-key")
  public ResponseEntity<String> getStreamKey(Authentication authentication) {
    String streamKey = userService.getStreamKey(authentication);
    return ResponseEntity.ok(streamKey);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/refresh-stream-key")
  public ResponseEntity<String> refreshStreamKey(Authentication authentication) {
    String newStreamKey = userService.refreshStreamKey(authentication);
    return ResponseEntity.ok(newStreamKey);
  }
}

