package semicolon.viewtist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Value("${stomp.test-server}")
  private String testServer;
  @Value("${stomp.deploy-server}")
  private String deployServer;
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/live/chat/")
        .allowedOrigins(testServer,deployServer)
        .allowedMethods("GET", "POST")
        .allowCredentials(true);
  }
}