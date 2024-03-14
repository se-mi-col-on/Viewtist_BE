package semicolon.viewtist.user.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.viewtist.jwt.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional
  @Async
  @Scheduled(cron = "0 0 0 * * *")
  public void autoDelete() {
    log.info("24시간이 지난 refresh token이 삭제되었습니다.");
    refreshTokenRepository.deleteByCreatedAtLessThanEqual(LocalDateTime.now().minusDays(1));
  }
}
