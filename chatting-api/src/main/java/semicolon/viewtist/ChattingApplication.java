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
@SpringBootApplication
public class ChattingApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChattingApplication.class, args);
  }
  @Bean
  public ServletWebServerFactory servletContainer() {
    // Enable SSL Trafic
    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
      @Override
      protected void postProcessContext(Context context) {
        SecurityConstraint securityConstraint = new SecurityConstraint();
        securityConstraint.setUserConstraint("CONFIDENTIAL");
        SecurityCollection collection = new SecurityCollection();
        collection.addPattern("/*");
        securityConstraint.addCollection(collection);
        context.addConstraint(securityConstraint);
      }
    };
    // Add HTTP to HTTPS redirect
    tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector());
    return tomcat;
  }

  private Connector httpToHttpsRedirectConnector() {
    Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
    connector.setScheme("http");
    connector.setPort(8081);
    connector.setSecure(false);
    connector.setRedirectPort(443);
    return connector;
  }
}