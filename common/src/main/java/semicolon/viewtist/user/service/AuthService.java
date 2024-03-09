package semicolon.viewtist.user.service;

import static semicolon.viewtist.global.exception.ErrorCode.ALREADY_EXISTS_EMAIL;
import static semicolon.viewtist.global.exception.ErrorCode.ALREADY_EXISTS_NICKNAME;
import static semicolon.viewtist.global.exception.ErrorCode.EMAIL_NOT_FUND;
import static semicolon.viewtist.global.exception.ErrorCode.INVALID_TOKEN;
import static semicolon.viewtist.global.exception.ErrorCode.PASSWORDS_NOT_MATCH;
import static semicolon.viewtist.global.exception.ErrorCode.PROCEED_EMAIL_VERIFICATION;
import static semicolon.viewtist.global.exception.ErrorCode.TIME_OUT_INVALID_TOKEN;
import static semicolon.viewtist.global.exception.ErrorCode.USER_NOT_FOUND;
import static semicolon.viewtist.global.exception.ErrorCode.VERIFY_YOUR_EMAIL;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import semicolon.viewtist.jwt.TokenProvider;
import semicolon.viewtist.jwt.entity.TokenBlacklist;
import semicolon.viewtist.jwt.repository.TokenBlacklistRepository;
import semicolon.viewtist.mailgun.MailgunClient;
import semicolon.viewtist.mailgun.SendMailForm;
import semicolon.viewtist.user.dto.request.UserSiginupRequest;
import semicolon.viewtist.user.dto.request.UserSigninRequest;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final TokenBlacklistRepository tokenBlacklistRepository;
  private final MailgunClient mailgunClient;
  private final TokenProvider tokenProvider;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  @Value("${mail.from}")
  private String from;
  @Value("${mail.signup.subject}")
  private String subject;
  @Value("${mail.signup.text}")
  private String text;
  @Value("${mail.verified.url}")
  private String verifiedUrl;

  // 회원가입
  public void signup(UserSiginupRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UserException(PROCEED_EMAIL_VERIFICATION));

    if (userRepository.existsByNickname(request.getNickname())) {
      throw new UserException(ALREADY_EXISTS_NICKNAME);
    }
    if (userRepository.existsByEmail(user.getEmail()) && user.isEmailVerified()) {
      user.setNickname(request.getNickname());
      user.setPassword(passwordEncoder.encode(request.getPassword()));
      user.setProfilePhotoUrl(request.getProfilePhotoUrl());

      userRepository.save(user);
    } else {
      throw new UserException(PROCEED_EMAIL_VERIFICATION);
    }
  }

  // 인증 이메일 발송
  public void sendEmail(String email) {
    // 이미 이메일이 있는지 확인
    if (userRepository.existsByEmail(email)) {
      throw new UserException(ALREADY_EXISTS_EMAIL);
    }

    String token = UUID.randomUUID().toString(); // 랜덤한 토큰 생성

    User user = User.builder()
        .email(email)
        .isEmailVerified(false)
        .emailVerificationToken(token)
        .tokenExpiryAt(LocalDateTime.now().plusMinutes(3)) // 3분 후 만료
        .type("local")
        .build();// 이메일 저장

    // User 객체에 토큰과 만료 시간 저장
    userRepository.save(user);

    String verificationLink =
        verifiedUrl + token; // 이메일 인증 링크 생성

    SendMailForm sendMailForm = SendMailForm.builder().from(from)
        .to(user.getEmail())
        .subject(subject)
        .text(text + verificationLink)
        .build();

    mailgunClient.sendEmail(sendMailForm);
  }

  // 이메일 인증
  public void verifyEmailToken(String token) {
    User user = userRepository.findByEmailVerificationToken(token)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    if (user.getTokenExpiryAt().isBefore(LocalDateTime.now())) {
      throw new UserException(TIME_OUT_INVALID_TOKEN); // 만료된 토큰일 경우 예외 발생
    }

    if (!(user.getEmailVerificationToken().equals(token))) {
      throw new UserException(INVALID_TOKEN); // 유효하지 않은 토큰일 경우 예외 발생
    }

    user.setEmailVerified(true, null); // 이메일 인증 상태를 true로 변경
    userRepository.save(user);
  }

  // 로그인
  public String signin(UserSigninRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UserException(EMAIL_NOT_FUND));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new UserException(PASSWORDS_NOT_MATCH);
    }

    if (!user.isEmailVerified()) {
      throw new UserException(VERIFY_YOUR_EMAIL);
    }

    return tokenProvider.generateToken(user);
  }

  // 로그아웃
  public void signout(String token) {
    addTokenBlacklist(token);
  }

  // 토큰 재발급
  public String refreshToken(String token) {

    addTokenBlacklist(token);

    return tokenProvider.refreshToken(token.substring(7));
  }

  private void addTokenBlacklist(String token) {

    TokenBlacklist tokenBlacklist = new TokenBlacklist(token.substring(7));
    tokenBlacklistRepository.save(tokenBlacklist);
  }
}


