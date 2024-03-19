package semicolon.viewtist.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semicolon.viewtist.user.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);

  Optional<User> findByEmail(String email);

  Optional<User> findByEmailVerificationToken(String token);

  Optional<User> findByNickname(String nickname);

  Optional<User> findBySessionId(String sessionId);
}
