package semicolon.viewtist;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"semicolon.viewtist.chatting.repository",
    "semicolon.viewtist.jwt.repository", "semicolon.viewtist.user.repository", "semicolon.viewtist.sse.repository"
    ,"semicolon.viewtist.liveStreaming.repository"})
@EntityScan(basePackages = {"semicolon.viewtist.chatting.entity",
    "semicolon.viewtist.jwt.entity", "semicolon.viewtist.user.entity", "semicolon.viewtist.sse.entity"
,"semicolon.viewtist.liveStreaming.entity"})
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ChattingApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChattingApplication.class, args);
  }
}
