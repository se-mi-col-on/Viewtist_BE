package semicolon.viewtist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableFeignClients(basePackages = "semicolon.viewtist.mailgun")
@EnableJpaRepositories(basePackages = {"semicolon.viewtist.user.repository",
    "semicolon.viewtist.jwt.repository", "semicolon.viewtist.image.repository"})
@EntityScan(basePackages = {"semicolon.viewtist.user.entity", "semicolon.viewtist.jwt.entity",
    "semicolon.viewtist.image.entity"})
@SpringBootApplication
@ImportAutoConfiguration({FeignAutoConfiguration.class, HttpClientConfiguration.class})
@ServletComponentScan
@EnableJpaAuditing
public class CommonApplication {

  public static void main(String[] args) {
    SpringApplication.run(CommonApplication.class, args);
  }
}