package semicolon.viewtist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = {"semicolon.viewtist.liveStreaming.repository",
    "semicolon.viewtist.user.repository", "semicolon.viewtist.jwt.repository",
    "semicolon.viewtist.sse.repository","semicolon.viewtist.chatting.repository"})
@EntityScan(basePackages = {"semicolon.viewtist.liveStreaming.entity",
    "semicolon.viewtist.user.entity", "semicolon.viewtist.jwt.entity",
    "semicolon.viewtist.sse.entity","semicolon.viewtist.chatting.entity"})
@EnableJpaAuditing
public class LiveStreamingApplication {

  public static void main(String[] args) {
    SpringApplication.run(LiveStreamingApplication.class, args);
  }
}