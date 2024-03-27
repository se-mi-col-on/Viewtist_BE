package semicolon.viewtist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/")
        .allowedOrigins("https://viewtist.vercel.app")
        .allowedMethods("GET", "POST")
        .allowCredentials(true);
  }
}