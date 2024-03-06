package semicolon.viewtis.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semicolon.viewtis.chatting.entity.ChatMessage;
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
