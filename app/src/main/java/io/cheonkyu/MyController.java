package io.cheonkyu;

import io.cheonkyu.mini.spring.stereotype.Controller;
import io.cheonkyu.mini.spring.web.bind.annotation.GetMapping;

@Controller
public class MyController {
  private final MyService myService;

  public MyController(MyService myService) {
    this.myService = myService;
  }

  @GetMapping("/api/hello-world")
  public String test() {
    return myService.helloWorld();
  }
}
