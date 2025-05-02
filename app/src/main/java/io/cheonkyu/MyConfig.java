package io.cheonkyu;

import io.cheonkyu.mini.spring.context.annotation.Bean;
import io.cheonkyu.mini.spring.context.annotation.Configuration;

@Configuration
public class MyConfig {
  @Bean
  public String test() {
    return "stest";
  }
}
