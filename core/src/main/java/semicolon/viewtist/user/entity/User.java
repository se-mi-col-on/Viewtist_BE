package semicolon.viewtist.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

  @Setter
  @Column
  private boolean isEmailVerified;

  @Setter
  @Column
  private String emailVerificationToken;

  @Column
  private String type;

  public User(String userId, String type, String email, String profilePhotoUrl) {
    this.nickname = userId;
    this.type = type;
    this.email = email;
    this.password = "******";
    this.profilePhotoUrl = profilePhotoUrl;
    this.isEmailVerified = true;
  }
}
