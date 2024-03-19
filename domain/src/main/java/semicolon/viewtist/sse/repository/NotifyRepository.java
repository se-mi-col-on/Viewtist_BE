package semicolon.viewtist.sse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semicolon.viewtist.sse.entity.Notify;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {

}