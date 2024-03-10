package semicolon.viewtist.oauth;

import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import semicolon.viewtist.user.dto.request.SocialUserRequest;
import semicolon.viewtist.user.entity.Type;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    String oauthClientName = userRequest.getClientRegistration().getClientName();

//    사용자 정보 출력을 위해 주석 처리
//    try {
//      System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }

    User user = null;
    String userId = null;
    String photoUrl;
    String email;
    SocialUserRequest socialUserRequest;
    if (oauthClientName.equals("kakao")) {
      userId = "kakao_" + oAuth2User.getAttributes().get("id");
      Map<String, String> properties = (Map<String, String>) oAuth2User.getAttributes()
          .get("properties");
      photoUrl = properties.get("profile_image");
      socialUserRequest = SocialUserRequest.builder()
          .userId(userId)
          .type(Type.kakao)
          .profilePhotoUrl(photoUrl)
          .email(userId + "@email.com")
          .build();
      user = SocialUserRequest.from(socialUserRequest);
    }

    if (oauthClientName.equals("naver")) {
      Map<String, String> response = (Map<String, String>) oAuth2User.getAttributes()
          .get("response");
      userId = "naver_" + response.get("id").substring(0, 14);
      email = response.get("email");
      photoUrl = response.get("profile_image");
      socialUserRequest = SocialUserRequest.builder()
          .userId(userId)
          .type(Type.naver)
          .profilePhotoUrl(photoUrl)
          .email(email)
          .build();
      user = SocialUserRequest.from(socialUserRequest);
    }

    if (oauthClientName.equals("Google")) {
      userId = "google_" + oAuth2User.getAttributes().get("sub");
      email = oAuth2User.getAttributes().get("email").toString();
      photoUrl = oAuth2User.getAttributes().get("picture").toString();

      socialUserRequest = SocialUserRequest.builder()
          .userId(userId)
          .type(Type.google)
          .profilePhotoUrl(photoUrl)
          .email(email)
          .build();

      user = SocialUserRequest.from(socialUserRequest);
    }
    userRepository.save(Objects.requireNonNull(user));
    return new CustomOAuth2User(userId);
  }
}