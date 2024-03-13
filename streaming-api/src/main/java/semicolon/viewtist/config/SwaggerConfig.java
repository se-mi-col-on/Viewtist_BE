package semicolon.viewtist.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .title("Viewtist API Document")
        .version("v0.0.1")
        .description("뷰티스트 스트리밍 서비스 명세서입니다.");

    String jwtSchemeName = "jwtAuth";

    SecurityRequirement securityRequirement = new SecurityRequirement()
        .addList(jwtSchemeName);

    Components components = new Components()
        .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
            .name(jwtSchemeName)
            .type(Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT"));

    return new OpenAPI()
        .info(info)
        .components(components)
        .addSecurityItem(securityRequirement);

  }
}