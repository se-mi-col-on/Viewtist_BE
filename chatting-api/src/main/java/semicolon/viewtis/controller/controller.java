package semicolon.viewtis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
  @GetMapping("/")
  public String chatting(){
    return "chatting";
  }


}
