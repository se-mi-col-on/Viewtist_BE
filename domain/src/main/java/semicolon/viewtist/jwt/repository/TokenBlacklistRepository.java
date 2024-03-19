package semicolon.viewtist.jwt.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semicolon.viewtist.jwt.entity.TokenBlacklist;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

  boolean existsByToken(String token);

  void deleteByCreatedAtLessThanEqual(LocalDateTime localDateTime);
}
