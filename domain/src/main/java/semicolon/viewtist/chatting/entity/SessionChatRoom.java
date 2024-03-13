package semicolon.viewtist.chatting.entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "sessionChatRoom")
@Builder
public class SessionChatRoom {
  @Id
  private String streamKey;
  @Indexed
  private String sessionId;

}
