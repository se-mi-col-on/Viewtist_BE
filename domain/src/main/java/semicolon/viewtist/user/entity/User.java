package semicolon.viewtist.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.viewtist.global.entitiy.BaseTimeEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String email;

  @Column
  private String nickname;

  @Column
  private String password;

  @Column
  private String profilePhotoUrl;

  @JoinColumn(name = "account_id")
  private Long accountId;

  @Column
  private boolean isEmailVerified;

  @Column
  private String emailVerificationToken;

  @Column
  private Type type;

  @Column
  private LocalDateTime tokenExpiryAt;

  @Column
  private String streamKey;

  @Column
  private String channelIntroduction;


  public void setEmailVerified(boolean emailVerified, String emailVerificationToken) {
    isEmailVerified = emailVerified;
    this.emailVerificationToken = emailVerificationToken;
  }

  public void setProfilePhotoUrl(String newImageUrl) {
    this.profilePhotoUrl = newImageUrl;
  }


  public void setToken(String token, LocalDateTime tokenExpiryAt) {
    this.emailVerificationToken = token;
    this.tokenExpiryAt = tokenExpiryAt;
  }


  public void setStreamKey(String streamKey) {
    this.streamKey = streamKey;
  }

  public void setSignup(String nickname, String password, String profilePhotoUrl) {
    this.nickname = nickname;
    this.password = password;
    this.profilePhotoUrl = profilePhotoUrl;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public void setChannelIntroduction(String updateIntroduction) {
    this.channelIntroduction = updateIntroduction;
  }
}
