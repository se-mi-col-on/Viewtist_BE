package semicolon.viewtist.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
  @OneToOne
  private Account account;

  @Column
  private boolean isEmailVerified;

  @Column
  private String emailVerificationToken;

  @Column
  private String type;

  @Column
  private LocalDateTime tokenExpiryDate;

  public User(String userId, String type, String email, String profilePhotoUrl) {
    this.nickname = userId;
    this.type = type;
    this.email = email;
    this.password = "******";
    this.profilePhotoUrl = profilePhotoUrl;
    this.isEmailVerified = true;
  }

  public void setEmailVerified(boolean emailVerified, String emailVerificationToken) {
    isEmailVerified = emailVerified;
    this.emailVerificationToken = emailVerificationToken;
  }

  public void setProfilePhotoUrl(String newImageUrl) {
    this.profilePhotoUrl = newImageUrl;
  }

  public void setPassword(String encode) {
    this.password = encode;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public void setToken(String token, LocalDateTime expiryDate) {
    this.emailVerificationToken = token;
    this.tokenExpiryDate = expiryDate;
  }
}
