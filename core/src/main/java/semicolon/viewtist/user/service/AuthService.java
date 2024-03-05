package semicolon.viewtist.user.service;

import static semicolon.viewtist.global.exception.ErrorCode.ALREADY_EXISTS_EMAIL;
import static semicolon.viewtist.global.exception.ErrorCode.ALREADY_EXISTS_NICKNAME;
import static semicolon.viewtist.global.exception.ErrorCode.EMAIL_NOT_FUND;
import static semicolon.viewtist.global.exception.ErrorCode.INVALID_TOKEN;
import static semicolon.viewtist.global.exception.ErrorCode.PASSWORDS_NOT_MATCH;
import static semicolon.viewtist.global.exception.ErrorCode.USER_NOT_FOUND;
import static semicolon.viewtist.global.exception.ErrorCode.VERIFY_YOUR_EMAIL;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import semicolon.viewtist.user.dto.UserDto;
import semicolon.viewtist.user.dto.request.UserSiginupRequest;
import semicolon.viewtist.user.dto.request.UserSigninRequest;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.jwt.TokenProvider;
import semicolon.viewtist.user.jwt.entity.TokenBlacklist;
import semicolon.viewtist.user.jwt.repository.TokenBlacklistRepository;
import semicolon.viewtist.user.mailgun.MailgunClient;
import semicolon.viewtist.user.mailgun.SendMailForm;
import semicolon.viewtist.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final TokenBlacklistRepository tokenBlacklistRepository;
  private final MailgunClient mailgunClient;
  private final TokenProvider tokenProvider;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void signup(UserSiginupRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new UserException(ALREADY_EXISTS_EMAIL);
    }

    if (userRepository.existsByNickname(request.getNickname())) {
      throw new UserException(ALREADY_EXISTS_NICKNAME);
    }
    User user = User.builder()
        .email(request.getEmail())
        .nickname(request.getNickname())
        .password(passwordEncoder.encode(request.getPassword()))
        .isEmailVerified(false)
        .build();// 비밀번호는 암호화하여 저장

    userRepository.save(user);
    sendEmail(user); // 이메일 전송
  }

  private void sendEmail(User user) {
    String token = UUID.randomUUID().toString(); // 랜덤한 토큰 생성
    user.setEmailVerificationToken(token); // 사용자에게 토큰 저장
    userRepository.save(user);

    String verificationLink =
        "http://localhost:8080/api/users/verify-email?token=" + token; // 이메일 인증 링크 생성

    SendMailForm sendMailForm = SendMailForm.builder()
        .from("test@gmail.com")
        .to(user.getEmail())
        .subject("Verification Email")
        .text("Please copy and paste the url: " +
            "\n" + verificationLink)
        .build();

    mailgunClient.sendEmail(sendMailForm);
  }

  public void verifyEmailToken(String token) {
    User user = userRepository.findByEmailVerificationToken(token)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    if (!(user.getEmailVerificationToken().equals(token))) {
      throw new UserException(INVALID_TOKEN); // 유효하지 않은 토큰일 경우 예외 발생
    }

    user.setEmailVerified(true); // 이메일 인증 상태를 true로 변경
    user.setEmailVerificationToken(null); // 토큰은 더 이상 필요하지 않으므로 null로 설정
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

    return tokenProvider.generateToken(UserDto.fromEntity(user));
  }

  // 로그아웃
  public void signout(String token) {
    // Blacklist에 토큰 추가
    TokenBlacklist tokenBlacklist = new TokenBlacklist(token.substring(7));
    tokenBlacklistRepository.save(tokenBlacklist);
  }

}


