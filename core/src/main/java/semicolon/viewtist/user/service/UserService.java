package semicolon.viewtist.user.service;

import static semicolon.viewtist.global.exception.ErrorCode.PASSWORDS_NOT_MATCH;
import static semicolon.viewtist.global.exception.ErrorCode.USER_NOT_FOUND;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import semicolon.viewtist.image.entity.Image;
import semicolon.viewtist.image.repository.ImageRepository;
import semicolon.viewtist.mailgun.MailgunClient;
import semicolon.viewtist.mailgun.SendMailForm;
import semicolon.viewtist.s3.s3Uploader.S3Uploader;
import semicolon.viewtist.user.dto.request.UpdatePasswordRequest;
import semicolon.viewtist.user.dto.response.UserDetailResponse;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final MailgunClient mailgunClient;
  private final ImageRepository imageRepository;
  private final S3Uploader s3Uploader;
  private final UserRepository userRepository;


  // 유저 정보
  public UserDetailResponse userDetail(Authentication authentication) {
    User user = findByEmailOrThrow(authentication.getName());
    return new UserDetailResponse(user);
  }

  private User findByEmailOrThrow(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
  }

  // 프로필 사진 변경
  public void updateProfilePhoto(MultipartFile newImageFile, Authentication authentication)
      throws IOException {
    User user = findByEmailOrThrow(authentication.getName());

    // 기존 프로필 사진이 있으면 삭제
    if (user.getProfilePhotoUrl() != null) {
      String oldImageFileName = getFileNameFromUrl(user.getProfilePhotoUrl());
      s3Uploader.deleteFileFromS3(oldImageFileName);

      // DB에서도 기존 이미지 정보 삭제
      Optional<Image> oldImageOptional = imageRepository.findByImageUrl(user.getProfilePhotoUrl());
      if (oldImageOptional.isPresent()) {
        Image oldImage = oldImageOptional.get();
        imageRepository.delete(oldImage);
      }

    }

    // 새로운 프로필 사진 업로드
    String newImageUrl = uploadPhoto(newImageFile, user.getEmail());

    // 사용자 정보에 새로운 프로필 사진 URL 업데이트
    user.setProfilePhotoUrl(newImageUrl);
    userRepository.save(user);
  }

  // 비밀번호 찾기
  public void findPassword(String email) {
    User user = findByEmailOrThrow(email);
    // 임시 비밀번호 지정
    String temporaryPassword = UUID.randomUUID().toString();
    user.setPassword(passwordEncoder.encode(temporaryPassword));
    userRepository.save(user);
    // 메일 발송
    SendMailForm sendMailForm = SendMailForm.builder().from("viewtist@gmail.com")
        .to(user.getEmail())
        .subject("temporary Password")
        .text(temporaryPassword + " is your temporary password." + "\n"
            + "Please change your password soon.")
        .build();

    mailgunClient.sendEmail(sendMailForm);
  }

  // 비밀번호 변경
  public void updatePassword(UpdatePasswordRequest updatePasswordRequest,
      Authentication authentication) {
    User user = findByEmailOrThrow(authentication.getName());
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

  // 스트림키 재발급
  public String reissueStreamKey(Authentication authentication) {
    User user = findByEmailOrThrow(authentication.getName());
    user.setStreamKey(UUID.randomUUID().toString());
    userRepository.save(user);
    return user.getStreamKey(); // 새로운 스트림키 반환
  }

  public static String getFileNameFromUrl(String url) {
    try {
      URL urlObj = new URL(url);
      String path = urlObj.getPath();
      return path.substring(path.lastIndexOf("/") + 1);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("잘못된 URL 형식입니다.", e);
    }
  }

  public String uploadPhoto(MultipartFile file, String email) throws IOException {
    String imageUrl = s3Uploader.upload(file, email);
    Image image = new Image();
    image.setImageUrl(imageUrl);
    imageRepository.save(image);
    return imageUrl;
  }


}
