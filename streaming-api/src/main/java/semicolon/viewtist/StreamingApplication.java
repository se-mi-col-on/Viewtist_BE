package semicolon.viewtist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = {"semicolon.viewtist.liveStreaming.repository",
    "semicolon.viewtist.user.repository", "semicolon.viewtist.jwt.repository"})
@EntityScan(basePackages = {"semicolon.viewtist.liveStreaming.entity",
    "semicolon.viewtist.user.entity", "semicolon.viewtist.jwt.entity"})
@EnableJpaAuditing
public class StreamingApplication {

  public static void main(String[] args) {
    SpringApplication.run(StreamingApplication.class, args);
  }
}