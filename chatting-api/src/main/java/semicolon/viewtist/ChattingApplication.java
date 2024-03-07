package semicolon.viewtist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "semicolon.viewtist.chatting.repository")
@EntityScan(basePackages = "semicolon.viewtist.chatting.entity")
@SpringBootApplication
public class ChattingApplication {
  public static void main(String[] args) {
    SpringApplication.run(ChattingApplication.class, args);
  }
}