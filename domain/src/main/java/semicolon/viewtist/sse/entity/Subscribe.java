package semicolon.viewtist.sse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.viewtist.global.entitiy.BaseTimeEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscribe extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String receiver;
  private String user;

}
