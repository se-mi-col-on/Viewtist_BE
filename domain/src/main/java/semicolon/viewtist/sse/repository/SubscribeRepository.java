package semicolon.viewtist.sse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semicolon.viewtist.sse.entity.Subscribe;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

  List<Subscribe> findByReceiver(String nickname);

  void deleteByReceiverAndUser(String receiverNickname, String userNickname);

  boolean existsByReceiverAndUser(String receiver, String user);
}

