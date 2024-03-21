package semicolon.viewtist.chatting.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semicolon.viewtist.chatting.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  boolean existsByStreamerId(Long streamerId);

  Page<ChatRoom> findByActiveIsTrue(Pageable pageable);

  Optional<ChatRoom> findByStreamingId(Long streamingId);
}
