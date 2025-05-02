package io.cheonkyu.mini.spring.boot;

import io.cheonkyu.mini.spring.context.ApplicationContext;

public class SpringApplication {
  // public static ApplicationContext run(Class<?> primarySource, String... args)
  // {
  // new SpringApplication();
  // return SpringApplication.run(primarySource, args);
  // }

  public ApplicationContext run() {
    // public ApplicationContext run(Class<?> primarySource, String... args) {
    final var context = createApplicationContext();
    return context;
  }

  public ApplicationContext createApplicationContext() {
    return new ApplicationContext("io.cheonkyu");
  }
}
