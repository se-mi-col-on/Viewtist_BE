package semicolon.viewtist.user.service;

import static semicolon.viewtist.global.exception.ErrorCode.ALREADY_EXISTS_EMAIL;
import static semicolon.viewtist.global.exception.ErrorCode.ALREADY_EXISTS_NICKNAME;
import static semicolon.viewtist.global.exception.ErrorCode.INVALID_TOKEN;
import static semicolon.viewtist.global.exception.ErrorCode.NOT_VERIFIED_EMAIL;
import static semicolon.viewtist.global.exception.ErrorCode.PASSWORDS_NOT_MATCH;
import static semicolon.viewtist.global.exception.ErrorCode.TIME_OUT_INVALID_TOKEN;
import static semicolon.viewtist.global.exception.ErrorCode.USER_NOT_FOUND;
import static semicolon.viewtist.global.exception.ErrorCode.VERIFY_YOUR_EMAIL;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.viewtist.jwt.TokenProvider;
import semicolon.viewtist.jwt.entity.RefreshToken;
import semicolon.viewtist.jwt.entity.TokenBlacklist;
import semicolon.viewtist.jwt.repository.RefreshTokenRepository;
import semicolon.viewtist.jwt.repository.TokenBlacklistRepository;
import semicolon.viewtist.mailgun.MailgunClient;
import semicolon.viewtist.mailgun.SendMailForm;
import semicolon.viewtist.user.dto.request.UserSigninRequest;
import semicolon.viewtist.user.dto.request.UserSignupRequest;
import semicolon.viewtist.user.dto.response.SigninResponse;
import semicolon.viewtist.user.entity.Type;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class AuthService {


  private final RefreshTokenRepository refreshTokenRepository;
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
  @Transactional
  public void signup(UserSignupRequest request) {
    User user = findUserByEmail(request.getEmail());

    if (userRepository.existsByNickname(request.getNickname())) {
      throw new UserException(ALREADY_EXISTS_NICKNAME);
    }

    if (!user.isEmailVerified()) {
      throw new UserException(NOT_VERIFIED_EMAIL);
    }

    user.setSignup(request.getNickname(), passwordEncoder.encode(request.getPassword()),
        request.getProfilePhotoUrl());

    userRepository.save(user);
  }

  // 이메일 보내기
  public void sendEmail(String email) {
    String token = UUID.randomUUID().toString();
    LocalDateTime tokenExpiryAt = LocalDateTime.now().plusMinutes(3);

    User user = userRepository.findByEmail(email).orElseGet(() -> User.builder()
        .email(email)
        .isEmailVerified(false)
        .emailVerificationToken(token)
        .tokenExpiryAt(tokenExpiryAt)
        .type(Type.local)
        .streamKey(UUID.randomUUID().toString())
        .channelIntroduction("채널을 소개해 주세요")
        .build());

    if (user.isEmailVerified()) {
      throw new UserException(ALREADY_EXISTS_EMAIL);
    }

    user.setToken(token, tokenExpiryAt);
    userRepository.save(user);

    String verificationLink = verifiedUrl + token;
    SendMailForm sendMailForm = SendMailForm.builder().from(from)
        .to(email)
        .subject(subject)
        .text(text + verificationLink)
        .build();
    mailgunClient.sendEmail(sendMailForm);
  }

  // 이메일 인증
  @Transactional
  public void verifyEmailToken(String token) {
    User user = userRepository.findByEmailVerificationToken(token)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    if (user.getTokenExpiryAt().isBefore(LocalDateTime.now())) {
      throw new UserException(TIME_OUT_INVALID_TOKEN);
    }

    user.setEmailVerified(true, null);
    userRepository.save(user);
  }

  // 로그인
  public SigninResponse signin(UserSigninRequest request) {
    User user = findUserByEmail(request.getEmail());

    validateUserCredentials(request.getPassword(), user);

    // 액세스 토큰 및 리프레시 토큰 생성 및 저장
    String accessToken = tokenProvider.generateToken(user);
    String refreshToken = generateAndPersistRefreshToken(accessToken);

    return new SigninResponse(accessToken, refreshToken);
  }

  // 로그아웃
  public void signout(String token) {
    invalidateTokens(token);
  }

  // 토큰 재발급
  @Transactional
  public String refreshToken(String accessToken, String refreshToken) {
    String accessTokenEmail = extractEmailFromToken(accessToken);

    if (!refreshTokenRepository.existsByRefreshToken(refreshToken)) {
      throw new UserException(INVALID_TOKEN);
    }

    User user = findUserByEmail(accessTokenEmail);

    String oldAccessToken = tokenProvider.getAccessTokenFromToken(refreshToken);

    String oldAccessTokenEmail = tokenProvider.getAccessTokenFromToken(oldAccessToken);

    if (!accessTokenEmail.equals(oldAccessTokenEmail)) {
      throw new UserException(INVALID_TOKEN);
    }

    validateRefreshToken(refreshToken);


    return tokenProvider.generateToken(user);
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
  }

  // 유저 비밀번호, 이메일 맞는지
  private void validateUserCredentials(String rawPassword, User user) {
    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
      throw new UserException(PASSWORDS_NOT_MATCH);
    }
    if (!user.isEmailVerified()) {
      throw new UserException(VERIFY_YOUR_EMAIL);
    }
  }

  // 리프레시 토큰 생성 및 저장
  private String generateAndPersistRefreshToken(String accessToken) {
    String token = tokenProvider.generateRefreshToken(accessToken);
    RefreshToken refreshToken = RefreshToken.builder().refreshToken(token).build();
    refreshTokenRepository.save(refreshToken);
    return token;
  }

  // 토큰 블랙리스트 추가 및 액세스 토큰 삭제
  private void invalidateTokens(String token) {
    addTokenBlacklist(token);
  }

  // 토큰에서 유저 아이디 추출(유저 아이디 사용하여 유저 정보 조회)
  private String extractEmailFromToken(String token) {
    return tokenProvider.getAccessTokenFromToken(token.substring(7));
  }

  // 리프레시 토큰 유효성 검사(토큰 유효성 검사)
  private void validateRefreshToken(String refreshToken) {
    if (!tokenProvider.validateToken(refreshToken)) {
      throw new UserException(INVALID_TOKEN);
    }
  }

  // 토큰 블랙리스트 추가
  private void addTokenBlacklist(String token) {
    TokenBlacklist tokenBlacklist = new TokenBlacklist(token.substring(7));
    tokenBlacklistRepository.save(tokenBlacklist);
  }


}



