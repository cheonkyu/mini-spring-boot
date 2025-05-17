package io.cheonkyu.mini.spring.context;

import java.util.HashMap;
import java.util.Map;

import io.cheonkyu.mini.spring.beans.BeanFactory;
import io.cheonkyu.mini.spring.context.scanner.AnnotationScanner;

public class ApplicationContext {
  private final BeanFactory beanFactory = new BeanFactory();

  public ApplicationContext(String basePackage) {
    try {
      final var scanner = new AnnotationScanner();
      scanner.init(basePackage);
      final var clazzes = scanner.getClazzes();
      for (var clazz : clazzes) {
        beanFactory.createBean(clazz);
      }
    } catch (Exception e) {
      System.out.println(e);
      throw new RuntimeException("Context init failed", e);
    }
  }

  public <T> T getBean(Class<T> clazz) {
    return beanFactory.getBean(clazz);
  }
}
