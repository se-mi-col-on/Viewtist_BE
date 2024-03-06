package semicolon.viewtist.image.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semicolon.viewtist.image.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

  Optional<Image> findByImageUrl(String profilePhotoUrl);
}

