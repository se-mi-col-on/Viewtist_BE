package semicolon.viewtist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"semicolon.viewtist.chatting.repository",
    "semicolon.viewtist.jwt.repository", "semicolon.viewtist.user.repository"})
@EntityScan(basePackages = {"semicolon.viewtist.chatting.entity",
    "semicolon.viewtist.jwt.entity", "semicolon.viewtist.user.entity"})
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ChattingApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChattingApplication.class, args);
  }
}