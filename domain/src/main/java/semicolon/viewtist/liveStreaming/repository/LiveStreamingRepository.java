package semicolon.viewtist.liveStreaming.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.viewtist.liveStreaming.entity.Category;
import semicolon.viewtist.liveStreaming.entity.LiveStreaming;
import semicolon.viewtist.user.entity.User;

public interface LiveStreamingRepository extends JpaRepository<LiveStreaming, Long> {

  Optional<LiveStreaming> findByUser(User user);

  Page<LiveStreaming> findAllByOrderByViewerCountDesc(Pageable pageable);

  Page<LiveStreaming> findAllByCategory(Category category, Pageable pageable);
}
