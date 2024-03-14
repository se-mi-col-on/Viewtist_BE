package semicolon.viewtist.jwt.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semicolon.viewtist.jwt.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  boolean existsByRefreshToken(String refreshToken);

  void deleteByCreatedAtLessThanEqual(LocalDateTime localDateTime);
}
