package semicolon.viewtist.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .title("Viewtist API Document")
        .version("v0.0.1")
        .description("뷰티스트 실시간 채팅 api 명세서입니다.");
    return new OpenAPI()
        .info(info);
  }
}