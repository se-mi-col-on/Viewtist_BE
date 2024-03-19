package semicolon.viewtist.user.service;

import static semicolon.viewtist.global.exception.ErrorCode.ALREADY_EXISTS_NICKNAME;
import static semicolon.viewtist.global.exception.ErrorCode.PASSWORDS_NOT_MATCH;
import static semicolon.viewtist.global.exception.ErrorCode.USER_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import semicolon.viewtist.image.entity.Image;
import semicolon.viewtist.image.repository.ImageRepository;
import semicolon.viewtist.mailgun.MailgunClient;
import semicolon.viewtist.mailgun.SendMailForm;
import semicolon.viewtist.s3.S3UploaderService;
import semicolon.viewtist.user.dto.request.UpdateIntroduction;
import semicolon.viewtist.user.dto.request.UpdateNickname;
import semicolon.viewtist.user.dto.request.UpdatePasswordRequest;
import semicolon.viewtist.user.dto.response.UserResponse;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final MailgunClient mailgunClient;
  private final ImageRepository imageRepository;
  private final S3UploaderService s3UploaderService;
  private final UserRepository userRepository;
  @Value("${mail.from}")
  private String from;
  @Value("${mail.password.subject}")
  private String subject;
  @Value("${mail.password.text}")
  private String text;
  @Value("${photo.url}")
  private String photoUrl;

  // 유저 정보
  public UserResponse userDetail(Authentication authentication) {
    User user = findByEmail(authentication.getName());
    return UserResponse.from(user);
  }

  private User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
  }

  // 프로필 사진 변경
  @Transactional
  public String updateProfilePhoto(MultipartFile newImageFile, Authentication authentication)
      throws IOException {
    User user = findByEmail(authentication.getName());

    // DB에서도 기존 이미지 정보 삭제
    Optional<Image> oldImageOptional = imageRepository.findByImageUrl(user.getProfilePhotoUrl());
    if (oldImageOptional.isPresent()) {
      Image oldImage = oldImageOptional.get();
      imageRepository.delete(oldImage);
    }

    // s3 버킷에 있으면 삭제
    if (user.getProfilePhotoUrl() != null) {
      s3UploaderService.deleteFileFromS3(user.getProfilePhotoUrl());
    }

    // 새로운 프로필 사진 업로드
    String newImageUrl = s3UploaderService.uploadImage(newImageFile);

    // 사용자 정보에 새로운 프로필 사진 URL 업데이트
    user.setProfilePhotoUrl(newImageUrl);
    userRepository.save(user);
    return newImageUrl;
  }

  // 프로필사진 기본으로 돌아가기
  @Transactional
  public String resetProfilePhoto(Authentication authentication) {
    User user = findByEmail(authentication.getName());
    user.setProfilePhotoUrl(photoUrl);
    userRepository.save(user);
    return photoUrl;
  }

  // 비밀번호 찾기
  @Transactional
  public void resetAndSendTemporaryPassword(String email) {
    User user = findByEmail(email);
    // 임시 비밀번호 지정
    String temporaryPassword = generateTemporaryPassword();
    user.setPassword(passwordEncoder.encode(temporaryPassword));
    userRepository.save(user);
    // 메일 발송
    sendTemporaryPasswordEmail(user.getEmail(), temporaryPassword);
  }

  private String generateTemporaryPassword() {
    return UUID.randomUUID().toString();
  }

  private void sendTemporaryPasswordEmail(String to, String temporaryPassword) {
    SendMailForm sendMailForm = SendMailForm.builder().from(from)
        .to(to)
        .subject(subject)
        .text(text + temporaryPassword)
        .build();

    mailgunClient.sendEmail(sendMailForm);
  }

  // 비밀번호 변경
  @Transactional
  public void updatePassword(UpdatePasswordRequest updatePasswordRequest,
      Authentication authentication) {
    User user = findByEmail(authentication.getName());
    if (!passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), user.getPassword())) {
      throw new UserException(PASSWORDS_NOT_MATCH);
    }
    if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getCheckPassword())) {
      throw new UserException(PASSWORDS_NOT_MATCH); // 새로운 비밀번호와 비밀번호 확인이 일치하지 않으면 예외 발생
    }
    user.setPassword(
        passwordEncoder.encode(updatePasswordRequest.getCheckPassword())); // 새로운 비밀번호로 업데이트

    userRepository.save(user);
  }


  // 유저 닉네임 수정
  @Transactional
  public void updateNickname(UpdateNickname updateNickname,
      Authentication authentication) {
    User user = findByEmail(authentication.getName());

    // 닉네임 중복 확인
    if (userRepository.existsByNickname(updateNickname.getNickname())) {
      throw new UserException(ALREADY_EXISTS_NICKNAME);
    }

    // 닉네임과 업데이트
    user.setNickname(updateNickname.getNickname());

    userRepository.save(user);
  }

  // 유저 소개글 수정
  @Transactional
  public void updateIntroduction(UpdateIntroduction updateIntroduction,
      Authentication authentication) {
    User user = findByEmail(authentication.getName());

    user.setChannelIntroduction(updateIntroduction.getIntroduction());

    userRepository.save(user);
  }

  // 스트림키 가져오기
  public String getStreamKey(Authentication authentication) {
    User user = findByEmail(authentication.getName());
    return user.getStreamKey();
  }

  // 새로운 스트림키 생성
  @Transactional
  public String refreshStreamKey(Authentication authentication) {
    User user = findByEmail(authentication.getName());
    user.setStreamKey(UUID.randomUUID().toString());
    userRepository.save(user); // 새로운 스트림 키 생성 후 저장
    return user.getStreamKey();
  }
}
