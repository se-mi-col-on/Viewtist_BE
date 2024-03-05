package semicolon.viewtist.user.mailgun;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "${mailgun.api.url}")
@Qualifier("mailgun")
public interface MailgunClient {

  @PostMapping("${mailgun.sandbox.domain}/messages")
  void sendEmail(@SpringQueryMap SendMailForm form);
}

