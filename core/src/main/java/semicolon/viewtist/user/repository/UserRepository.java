package semicolon.viewtist.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.viewtist.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);

  Optional<User> findByEmail(String email);

  Optional<User> findByEmailVerificationToken(String token);
}
