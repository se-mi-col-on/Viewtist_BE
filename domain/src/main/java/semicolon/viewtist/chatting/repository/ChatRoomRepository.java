package semicolon.viewtist.chatting.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semicolon.viewtist.chatting.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  boolean existsByStreamKey(String streamKey);
  boolean findByStreamKeyAndActiveIsTrue(String streamKey);
  Optional<ChatRoom> findByStreamerId(Long streamerId);
  List<ChatRoom> findByActiveIsTrue();

  Optional<ChatRoom> findByStreamKey(String streamKey);
}
