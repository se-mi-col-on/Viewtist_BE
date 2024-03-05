package semicolon.viewtist.user.dto;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import semicolon.viewtist.user.entity.Account;
import semicolon.viewtist.user.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements UserDetails {

  private Long id;
  private String email;
  private String nickname;
  private String password;
  private String profilePhotoUrl;
  private Account account;
  private boolean isEmailVerified = false;
  private String emailVerificationToken;

  public static UserDto fromEntity(User user) {
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .password(user.getPassword())
        .profilePhotoUrl(user.getProfilePhotoUrl())
        .account(user.getAccount())
        .isEmailVerified(user.isEmailVerified())
        .build();
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
