package semicolon.viewtist.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/live/chat/")
        .allowedOrigins("http://localhost:3000","http://localhost:5173")
        .allowedMethods("GET", "POST")
        .allowCredentials(true);
  }
}