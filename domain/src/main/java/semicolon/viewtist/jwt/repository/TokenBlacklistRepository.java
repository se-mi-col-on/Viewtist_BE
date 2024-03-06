package semicolon.viewtist.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.viewtist.jwt.entity.TokenBlacklist;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

  boolean existsByToken(String token);
}
