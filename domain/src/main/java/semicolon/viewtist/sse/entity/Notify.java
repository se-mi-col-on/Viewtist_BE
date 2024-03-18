package semicolon.viewtist.sse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import semicolon.viewtist.global.entitiy.BaseTimeEntity;
import semicolon.viewtist.user.entity.User;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notify extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notification_id")
  private Long id;

  private String content;

  @Column(nullable = false)
  private Boolean isRead;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType notificationType;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User receiver;


  public enum NotificationType{
    STREAMING, VIEWTIST, TEST, RESEND,
  }
}
