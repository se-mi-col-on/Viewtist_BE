package semicolon.viewtist.user.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.viewtist.user.jwt.entity.TokenBlacklist;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

  boolean existsByToken(String token);
}
